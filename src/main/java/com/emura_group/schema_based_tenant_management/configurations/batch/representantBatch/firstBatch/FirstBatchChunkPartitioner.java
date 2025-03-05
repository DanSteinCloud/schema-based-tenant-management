package com.emura_group.schema_based_tenant_management.configurations.batch.representantBatch.firstBatch;

import com.emura_group.schema_based_tenant_management.repository.batch.RepresentantRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Component("firstBatchPartitioner")
public class FirstBatchChunkPartitioner implements Partitioner {
    //private final RepresentantRepository representantRepository;
    @Qualifier("representantDataSource")
    private final DataSource representantDataSource ;
    //@PersistenceUnit

    @Qualifier("representantEntityManager")
    private final EntityManager entityManager;

    @Qualifier("representantEntityManagerFactory")
    private final EntityManagerFactory entityManagerFactory;

    @Qualifier("representantTransactionManager")
    private final PlatformTransactionManager representantTransactionManager;

    @Qualifier("representantJobRepository")
    private final JobRepository jobRepository;

    @Autowired
    @Qualifier("representantJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private final LocalDateTime yesterdayStart;
    private final LocalDateTime yesterdayEnd;
    private final int chunkSize = 5;

    @Autowired
    public FirstBatchChunkPartitioner(@Qualifier("representantDataSource") DataSource representantDataSource,
                                      @Qualifier("representantEntityManagerFactory") EntityManagerFactory entityManagerFactory,
                                      @Qualifier("representantEntityManager") EntityManager entityManager,
                                      @Qualifier("representantTransactionManager") PlatformTransactionManager representantTransactionManager,
                                      @Qualifier("representantJobRepository") JobRepository jobRepository) {
        this.representantDataSource = representantDataSource;
        this.entityManager = entityManager;
        this.entityManagerFactory = entityManagerFactory;
        this.jobRepository = jobRepository;
        this.representantTransactionManager= representantTransactionManager;

        //this.representantRepository = representantRepository;
        LocalDate yesterday = LocalDate.now().minusDays(1);
        this.yesterdayStart = yesterday.atStartOfDay();
        this.yesterdayEnd = yesterday.atTime(LocalTime.MAX);
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        //long totalRecords = representantRepository.countAllRecords(yesterdayStart,yesterdayEnd);
        Integer totalCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM representant", Integer.class);
        System.out.println("Total Records Satisfying the condition " + totalCount);
        int numberOfPartitions = (int) Math.ceil((double) totalCount / chunkSize);
        Map<String, ExecutionContext> partitions = new HashMap<>();

        for (int i = 0; i < numberOfPartitions; i++) {
            ExecutionContext context = new ExecutionContext();
            context.putInt("pageNumber", i);
            context.putInt("pageSize", chunkSize);
            partitions.put("partition" + i, context);
            System.out.println("Creating partition -" + i + " with pageNumber + " + i + " chunkSize " + chunkSize);
        }
        return partitions;
    }
}

