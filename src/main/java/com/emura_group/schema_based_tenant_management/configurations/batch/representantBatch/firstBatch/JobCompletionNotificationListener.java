package com.emura_group.schema_based_tenant_management.configurations.batch.representantBatch.firstBatch;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {
    @Qualifier("representantJdbcTemplate")
    private final JdbcTemplate jdbcTemplate;

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

    public JobCompletionNotificationListener(@Qualifier("representantDataSource") DataSource representantDataSource,
                                             @Qualifier("representantEntityManagerFactory") EntityManagerFactory entityManagerFactory,
                                             @Qualifier("representantEntityManager") EntityManager entityManager,
                                             @Qualifier("representantTransactionManager") PlatformTransactionManager representantTransactionManager,
                                             @Qualifier("representantJobRepository") JobRepository jobRepository,
                                             @Qualifier("representantJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.representantDataSource = representantDataSource;
        this.entityManager = entityManager;
        this.entityManagerFactory = entityManagerFactory;
        this.jobRepository = jobRepository;
        this.representantTransactionManager = representantTransactionManager;
    }

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Job Started: " + jobExecution.getJobInstance().getJobName());
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        System.out.println("Job Finished: " + jobExecution.getJobInstance().getJobName());
        System.out.println("Job Status: " + jobExecution.getStatus());
    }
}




