package com.emura_group.schema_based_tenant_management.configurations.batch.representantBatch.firstBatch;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.reactive.function.client.WebClient;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing(dataSourceRef = "representantDataSource")
public class FirstBatchHelperBeans extends DefaultBatchConfiguration {
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

    public FirstBatchHelperBeans(@Qualifier("representantDataSource") DataSource representantDataSource,
                                 @Qualifier("representantEntityManager") EntityManager entityManager,
                                 @Qualifier("representantEntityManagerFactory") EntityManagerFactory entityManagerFactory,
                                 @Qualifier("representantTransactionManager") PlatformTransactionManager representantTransactionManager,
                                 @Qualifier("representantJobRepository") JobRepository jobRepository) {
        this.representantDataSource = representantDataSource;
        this.entityManager = entityManager;
        this.entityManagerFactory = entityManagerFactory;
        this.jobRepository = jobRepository;
        this.representantTransactionManager = representantTransactionManager;
    }

    @Override
    protected DataSource getDataSource() {
        return representantDataSource;
    }

    @Override
    protected PlatformTransactionManager getTransactionManager() {
        return this.representantTransactionManager;
    }

    @Bean
    public WebClient webClientBuilder() {
        return WebClient.builder().build();
    }
}
