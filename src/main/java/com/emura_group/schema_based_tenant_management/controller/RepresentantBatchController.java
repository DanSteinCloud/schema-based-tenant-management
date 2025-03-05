package com.emura_group.schema_based_tenant_management.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
@RequestMapping("/batch")
@EnableBatchProcessing(dataSourceRef = "representantDataSource")
public class RepresentantBatchController {
    @Qualifier("firstBatchJobLauncher")
    private final JobLauncher jobLauncher;
    @Qualifier("firstBatchJob")
    private final Job job;
    @Qualifier("representantDataSource")
    private final DataSource representantDataSource;

    @Qualifier("representantEntityManager")
    private final EntityManager entityManager;

    @Qualifier("representantEntityManagerFactory")
    private final EntityManagerFactory entityManagerFactory;

    @Qualifier("representantJobRepository")
    private final JobRepository jobRepository;

    @Qualifier("representantTransactionManager")
    private final PlatformTransactionManager transactionManager;
    public RepresentantBatchController(@Qualifier("firstBatchJobLauncher") JobLauncher jobLauncher,
                                       @Qualifier("firstBatchJob") Job job,
                                       @Qualifier("representantDataSource") DataSource representantDataSource,
                                       @Qualifier("representantEntityManager") EntityManager entityManager,
                                       @Qualifier("representantEntityManagerFactory") EntityManagerFactory entityManagerFactory,
                                       @Qualifier("representantTransactionManager") PlatformTransactionManager representantTransactionManager,
                                       @Qualifier("representantJobRepository") JobRepository jobRepository) {
        this.jobLauncher = jobLauncher;
        this.job = job;
        this.representantDataSource = representantDataSource;
        this.entityManager = entityManager;
        this.entityManagerFactory = entityManagerFactory;
        this.jobRepository = jobRepository;
        this.transactionManager = representantTransactionManager;
    }

    @GetMapping("/start-representant-batch")
    //every day at 1am
    //@Scheduled(cron = "0 0 1 1/1 * ?")

    public BatchStatus startBatch() throws Exception {

        System.out.println("batch started ............... ");

        JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
        while (jobExecution.isRunning()) {
            System.out.println("......");
        }

        System.out.println(jobExecution.getStatus());
        return jobExecution.getStatus();
    }
}

