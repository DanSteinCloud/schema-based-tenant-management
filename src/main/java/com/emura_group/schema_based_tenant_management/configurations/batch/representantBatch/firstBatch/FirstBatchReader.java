package com.emura_group.schema_based_tenant_management.configurations.batch.representantBatch.firstBatch;

import com.emura_group.schema_based_tenant_management.domain.model.Representant;

import com.emura_group.schema_based_tenant_management.mapper.RepresentantMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.reactive.function.client.WebClient;

import javax.sql.DataSource;
import java.time.LocalDateTime;

import java.util.Iterator;
import java.util.List;

@Component("firstBatchReader")
//@Qualifier("firstStepReader")
@StepScope
public class FirstBatchReader implements ItemReader<Representant> {
    private static final Logger LOGGER = LogManager.getLogger(FirstBatchReader.class);
    //private final JdbcPagingItemReader<Representant> pagingItemReader;
    private Iterator<Representant> currentBatch;
    private int pageNumber;
    private int pageSize;
    private final LocalDateTime yesterdayStart = null;
    private final LocalDateTime yesterdayEnd = null;

    @Qualifier("representantJdbcTemplate")
    private final JdbcTemplate jdbcTemplate;

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

    public FirstBatchReader(@Qualifier("representantDataSource") DataSource representantDataSource,
                            @Qualifier("representantEntityManagerFactory") EntityManagerFactory entityManagerFactory,
                            @Qualifier("representantEntityManager") EntityManager entityManager,
                            @Qualifier("representantTransactionManager") PlatformTransactionManager representantTransactionManager,
                            @Qualifier("representantJobRepository") JobRepository jobRepository,
                            @Qualifier("representantJdbcTemplate") JdbcTemplate jdbcTemplate) {
        //this.pagingItemReader = createPagingItemReader(dataSource);
        this.jdbcTemplate = jdbcTemplate;
        this.representantDataSource = representantDataSource;
        this.entityManager = entityManager;
        this.entityManagerFactory = entityManagerFactory;
        this.jobRepository = jobRepository;
        this.representantTransactionManager = representantTransactionManager;
    }

//    @Autowired
//    public void FirstStepListener(@Qualifier("representantDataSource") DataSource dataSource) {
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//    }

//    private JdbcPagingItemReader<Representant> createPagingItemReader(DataSource dataSource) {
//        JdbcPagingItemReader<Representant> reader = new JdbcPagingItemReader<>();
//        reader.setDataSource(dataSource);
//        reader.setPageSize(10); // Set your page size
//
//        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
//        queryProvider.setDataSource(dataSource);
//        queryProvider.setSelectClause("SELECT *");
//        queryProvider.setFromClause("FROM representant");
//        queryProvider.setSortKey("id"); // Required for pagination
//
//        try {
//            reader.setQueryProvider(queryProvider.getObject());
//        } catch (Exception e) {
//            throw new RuntimeException("Error setting up paging query provider", e);
//        }
//
//        reader.setRowMapper(new BeanPropertyRowMapper<>(Representant.class));
//        return reader;
//    }

//    @Bean
//    @Qualifier("firstStepReader")
//    public JdbcPagingItemReader<Representant> reader(@Qualifier("representantDataSource") DataSource dataSource) {
//        JdbcPagingItemReader<Representant> reader = new JdbcPagingItemReader<>();
//        reader.setDataSource(dataSource);
//        reader.setFetchSize(100);
//        reader.setRowMapper(new BeanPropertyRowMapper<>(Representant.class));
//
//        PostgresPagingQueryProvider queryProvider = new PostgresPagingQueryProvider();
//        queryProvider.setSelectClause("SELECT *");
//        queryProvider.setFromClause("FROM representant");
//        queryProvider.setSortKeys(new HashMap<>() {{
//            put("id", org.springframework.batch.item.database.Order.ASCENDING);
//        }});
//
//        reader.setQueryProvider(queryProvider);
//        return reader;
//    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getExecutionContext();
        this.pageNumber = executionContext.getInt("pageNumber");
        this.pageSize = executionContext.getInt("pageSize");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        // Example: Fetch total count
        Integer totalCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM representant", Integer.class);
        stepExecution.getExecutionContext().putInt("totalCount", totalCount);

        int pageNumber = 0; // Start from the first page
        boolean hasMoreData = true;

        List<Representant> representants = null;
        while (hasMoreData) {
            String sql = "SELECT * FROM representant ORDER BY id LIMIT ? OFFSET ?";
            representants = jdbcTemplate.query(sql, new Object[]{this.pageSize, pageNumber * this.pageSize},
                    new RepresentantMapper()
            );
            if (representants.isEmpty()) {
                hasMoreData = false;
            } else {
                processResults(representants);
                pageNumber++; // Move to the next page
            }
        }

        // Convert to Page using PageImpl

        Pageable pageable = Pageable.ofSize(10).withPage(0); // Example: page size = 10, first page
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), representants.size());
        Page<Representant> page = new PageImpl<>(representants.subList(start, end), pageable, representants.size());

        // 3. Store in ExecutionContext for later access
        executionContext.put("representantPage", page);

        currentBatch = page.hasContent() ? page.getContent().iterator() : null;
    }

    private void processResults(List<Representant> results) {
        // Process the paginated results
        results.forEach(System.out::println);
    }

    @Override
    public Representant read() throws Exception {
        // Return the next item from the batch, or null if no more data
        if (currentBatch != null && currentBatch.hasNext()) {
            return currentBatch.next();
        }

        return null;
    }
}


