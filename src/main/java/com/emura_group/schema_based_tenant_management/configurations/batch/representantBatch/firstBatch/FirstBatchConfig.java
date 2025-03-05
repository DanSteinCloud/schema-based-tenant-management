package com.emura_group.schema_based_tenant_management.configurations.batch.representantBatch.firstBatch;

import com.emura_group.schema_based_tenant_management.domain.model.Representant;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableBatchProcessing(dataSourceRef = "representantDataSource")
public class FirstBatchConfig {
    @Qualifier("representantDataSource")
    private final DataSource representantDataSource;

    @Qualifier("representantEntityManager")
    private final EntityManager entityManager;

    @Qualifier("representantEntityManagerFactory")
    private final EntityManagerFactory entityManagerFactory;

    @Qualifier("representantJobRepository")
    private final JobRepository jobRepository;

    @Qualifier("representantTransactionManager")
    private final PlatformTransactionManager representantTransactionManager;

    @StepScope
    private final FirstBatchReader reader;

    @StepScope
    private final FirstBatchWriter writer;

    @StepScope
    private final FirstBatchNoOpItemProcessor processor;

    private final Partitioner partitioner;

    private final JobCompletionNotificationListener listener;

    private final int chunkSize = 5000;

    @Autowired
    public FirstBatchConfig(@Qualifier("representantDataSource") DataSource representantDataSource,
                            @Qualifier("representantEntityManagerFactory") EntityManagerFactory entityManagerFactory,
                            @Qualifier("representantEntityManager") EntityManager entityManager,
                            @Qualifier("representantTransactionManager") PlatformTransactionManager representantTransactionManager,
                            @Qualifier("representantJobRepository") JobRepository jobRepository,
                            JobCompletionNotificationListener listener,
                            Partitioner partitioner,
                            FirstBatchNoOpItemProcessor processor,
                            FirstBatchReader reader,
                            FirstBatchWriter writer) {
        this.representantDataSource = representantDataSource;
        this.entityManager = entityManager;
        this.entityManagerFactory = entityManagerFactory;
        this.jobRepository = jobRepository;
        this.representantTransactionManager = representantTransactionManager;
        this.reader = reader;
        this.writer = writer;
        this.processor = processor;
        this.partitioner = partitioner;
        this.listener = listener;
    }

    @PostConstruct
    public JobRepository initialize() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(representantDataSource);
        factory.setTransactionManager(representantTransactionManager);
        factory.setDatabaseType(DatabaseType.POSTGRES.name());
        factory.setIsolationLevelForCreate("ISOLATION_SERIALIZABLE");
        factory.setTablePrefix("BATCH_");
        factory.afterPropertiesSet();
        return factory.getObject();
    }

//    @Override
//    protected DataSource getDataSource() {
//        return representantDataSource;
//    }

//    @Override
//    protected PlatformTransactionManager getTransactionManager() {
//        return this.representantTransactionManager;
//    }

    @Bean("firstBatchJob")
    public Job job(@Qualifier("representantJobRepository") JobRepository jobRepository){
        return new JobBuilder("firstBatchJob" , jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(leaderStep(jobRepository))
                .listener(listener)
                .build();
    }

    @Bean
    public Step leaderStep(@Qualifier("representantJobRepository") JobRepository jobRepository) {
        return new StepBuilder("leaderStep" , jobRepository)
                .partitioner("workerStep" , partitioner)
                .step(workerStep(jobRepository))
                .taskExecutor(taskExecutor(jobRepository))
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step workerStep(@Qualifier("representantJobRepository") JobRepository jobRepository) {
        return new StepBuilder("workerStep", jobRepository)
                .<Representant, Representant>chunk(chunkSize, representantTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor(@Qualifier("representantJobRepository") JobRepository jobRepository) {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        int cores = Runtime.getRuntime().availableProcessors();
        taskExecutor.setCorePoolSize(Math.max(cores - 1, 1));
        taskExecutor.setMaxPoolSize(Math.max(cores - 1, 1));
        taskExecutor.setQueueCapacity(64);
        taskExecutor.setThreadNamePrefix("BatchTask-");
        taskExecutor.initialize();
        return taskExecutor;
    }
}
