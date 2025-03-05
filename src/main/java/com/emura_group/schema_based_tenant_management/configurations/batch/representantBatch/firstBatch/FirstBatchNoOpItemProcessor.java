package com.emura_group.schema_based_tenant_management.configurations.batch.representantBatch.firstBatch;

import com.emura_group.schema_based_tenant_management.domain.model.Representant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Component("firstBatchProcessor")
@StepScope
public class FirstBatchNoOpItemProcessor implements ItemProcessor<Representant, Representant> {
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

    public FirstBatchNoOpItemProcessor(@Qualifier("representantDataSource") DataSource representantDataSource,
                                       @Qualifier("representantEntityManagerFactory") EntityManagerFactory entityManagerFactory,
                                       @Qualifier("representantEntityManager") EntityManager entityManager,
                                       @Qualifier("representantTransactionManager") PlatformTransactionManager representantTransactionManager,
                                       @Qualifier("representantJobRepository") JobRepository jobRepository) {
        this.representantDataSource = representantDataSource;
        this.entityManager = entityManager;
        this.entityManagerFactory = entityManagerFactory;
        this.jobRepository = jobRepository;
        this.representantTransactionManager = representantTransactionManager;
    }

    @Override
    public Representant process(Representant item) throws Exception {
        return item;
    }
}
