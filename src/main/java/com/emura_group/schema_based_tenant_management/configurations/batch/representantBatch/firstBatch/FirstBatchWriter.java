package com.emura_group.schema_based_tenant_management.configurations.batch.representantBatch.firstBatch;

import com.emura_group.schema_based_tenant_management.domain.model.Representant;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import reactor.core.publisher.Mono;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component("firstBatchWriter")
@StepScope
public class FirstBatchWriter implements ItemWriter<Representant> {

    private static final Logger LOGGER = LogManager.getLogger(FirstBatchWriter.class);
    private ThreadPoolExecutor executor;
    private final int parallelChunkSizeWriter = 2500;

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

    public FirstBatchWriter(@Qualifier("representantDataSource") DataSource representantDataSource,
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

    @PostConstruct
    public void postConstruct() {
        int parallelExecutionInWriter = 2;
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(parallelExecutionInWriter);
    }

    @Override
    public void write(Chunk<? extends Representant> chunk) throws Exception {
        LOGGER.info("Processing chunk " + parallelChunkSizeWriter + " representants");
        List<? extends Representant> reviews = chunk.getItems();

        List<List<Representant>> subChunks = createSubChunks(reviews, parallelChunkSizeWriter);

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (List<Representant> subChunk : subChunks) {
            futures.add(CompletableFuture.runAsync(() -> processSubChunk(subChunk), executor));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private List<List<Representant>> createSubChunks(List<? extends Representant> reviews, int chunkSize) {
        List<List<Representant>> subChunks = new ArrayList<>();
        for (int i = 0; i < reviews.size(); i += chunkSize) {
            subChunks.add(new ArrayList<>(reviews.subList(i, Math.min(i + chunkSize, reviews.size()))));
        }
        return subChunks;
    }

    private void processSubChunk(List<Representant> subChunk) {

        LOGGER.info("Processing chunk " + subChunk.size() + " representants");
        List<Mono<Void>> tasks = new ArrayList<>();

        for (Representant review : subChunk) {
            String newSchema = review.getCompanyName().replaceAll("\\s", "-");
        }
        subChunk.forEach(System.out::println);
    }

    //@Bean
    //@StepScope
//    public FlatFileItemWriter<Representant> fileWriter(List<Representant> subChunk) {
//        FlatFileItemWriter<Representant> writer = new FlatFileItemWriter<Representant>();
//        writer.setResource(new FileSystemResource("data/output.csv"));
//
//        writer.setHeaderCallback(new FlatFileHeaderCallback() {
//            @Override
//            public void writeHeader(java.io.Writer writer) throws IOException {
//                writer.write("id, country, isCompanyname, companyname, companyemail, last_update, ninea, firstname, " +
//                        "lastname, responsability_in_company, registrationdate, activatedat, " +
//                        "modifiedat, telephone, isactivated, isemailverified,isnineaverified, hasavalidcontrat");
//            }
//        });

//        DelimitedLineAggregator<Representant> lineAggregator = new DelimitedLineAggregator<Representant>();
//        lineAggregator.setDelimiter(",");
//
//        BeanWrapperFieldExtractor<Representant> fieldExtractor = new BeanWrapperFieldExtractor<Representant>();
//        fieldExtractor.setNames(new String[]{"id", "country", "isCompanyname", "companyname", "companyemail", "last_update", "ninea", "firstname", "lastname", "responsability_in_company", "registrationdate", "activatedat", "modifiedat", "telephone", "isactivated", "isemailverified", "isnineaverified", "hasavalidcontrat"});
//
//        lineAggregator.setFieldExtractor(fieldExtractor);
//
//        writer.setLineAggregator(lineAggregator);
//        return writer;
//    }
}

