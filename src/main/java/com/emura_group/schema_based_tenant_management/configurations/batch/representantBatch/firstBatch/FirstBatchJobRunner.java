package com.emura_group.schema_based_tenant_management.configurations.batch.representantBatch.firstBatch;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Component
public class FirstBatchJobRunner implements CommandLineRunner {

    @Qualifier("firstBatchJobLauncher")
    private final JobLauncher jobLauncher;

    @Qualifier("firstBatchJob")
    private final Job firstBatchJob;

    @Qualifier("representantDataSource")
    private final DataSource representantDataSource;

    @Qualifier("representantEntityManager")
    private final EntityManager entityManager;

    @Qualifier("representantEntityManagerFactory")
    private final EntityManagerFactory entityManagerFactory;

    @Qualifier("representantTransactionManager")
    private final PlatformTransactionManager representantTransactionManager;

    @Qualifier("representantJobRepository")
    private final JobRepository jobRepository;

    @Autowired
    public FirstBatchJobRunner(@Qualifier("firstBatchJobLauncher") JobLauncher jobLauncher,
                               @Qualifier("firstBatchJob") Job firstBatchJob,
                               @Qualifier("representantDataSource") DataSource representantDataSource,
                               @Qualifier("representantEntityManagerFactory") EntityManagerFactory entityManagerFactory,
                               @Qualifier("representantEntityManager") EntityManager entityManager,
                               @Qualifier("representantTransactionManager") PlatformTransactionManager representantTransactionManager,
                               @Qualifier("representantJobRepository") JobRepository jobRepository) {
        this.jobLauncher = jobLauncher;
        this.firstBatchJob = firstBatchJob;
        this.representantDataSource = representantDataSource;
        this.entityManager = entityManager;
        this.entityManagerFactory = entityManagerFactory;
        this.jobRepository = jobRepository;
        this.representantTransactionManager = representantTransactionManager;
    }

//    @Bean
//    public JobRepository jobRepository() throws Exception {
//        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
//        factory.setDataSource(dataSource); // Set your custom DataSource here
//        factory.setTransactionManager(transactionManager);
//        factory.afterPropertiesSet();
//        return factory.getObject();
//    }

    @Bean(name = "firstBatchJobLauncher")
    public JobLauncher jobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor()); // or any other TaskExecutor
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Override
    public void run(String... args) throws Exception {
        jobLauncher.run(firstBatchJob, new JobParameters());  // Start the job when the app starts
    }
}
