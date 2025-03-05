package com.emura_group.schema_based_tenant_management.datasource.batch;

import java.util.HashMap;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;

import org.springframework.batch.support.DatabaseType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilderCustomizer;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@EnableAspectJAutoProxy(proxyTargetClass = true)
@Profile("batch")
@PropertySource({"classpath:application-batch.properties"})
@EnableBatchProcessing(dataSourceRef = "representantDataSource")
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "representantEntityManagerFactory",
        transactionManagerRef = "representantTransactionManager", basePackages = {
        "com.emura_group.schema_based_tenant_management.repository.batch"})
public class PersistenceRepresentantConfiguration extends DefaultBatchConfiguration {
    @Autowired
    private Environment env;

    public PersistenceRepresentantConfiguration() {
        super();
    }

    @Primary
    @Bean(name = "representantDataSourceProperties")
    @ConfigurationProperties("spring.datasource.representant")
    public DataSourceProperties firstDataSourceProperties() {
        return new DataSourceProperties();
    }

//    @Primary
//    @BatchDataSource
//    @Bean(name = "representantDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.representant")
//    public DataSource representantDataSource() {
//        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl("jdbc:postgresql://localhost:5432/em_db?currentSchema=assiganto");
//        dataSource.setUsername("postgres");
//        dataSource.setPassword("@Nathalie2024");
//
//        return dataSource;
//    }



//    @Primary
//    //@BatchDataSource
//    @Bean(name = "representantDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.representant")
//    public DataSource representantDataSource() {
//        return DataSourceBuilder.create().type(HikariDataSource.class).build();
//    }

//    @Primary
//    @Bean(name = "entityManagerFactoryBuilder")
//    public EntityManagerFactoryBuilder entityManagerFactoryBuilder(
//            ObjectProvider<PersistenceUnitManager> persistenceUnitManager) {
//        return new EntityManagerFactoryBuilder(
//                new HibernateJpaVendorAdapter(), new HashMap<>(), persistenceUnitManager.getIfAvailable());
//    }

    @Primary
    //@BatchDataSource
    @Bean(name = "entityManagerFactoryBuilderCustomizer")
    public EntityManagerFactoryBuilderCustomizer entityManagerFactoryBuilderCustomizer() {
        return builder -> {
            // Customize your EntityManagerFactoryBuilder if needed
        };
    }

    @Primary
    //@BatchDataSource
    @Bean(name = "representantEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder representantEntityManagerFactoryBuilder(
            @Qualifier("entityManagerFactoryBuilderCustomizer") EntityManagerFactoryBuilderCustomizer customizer) {

        EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(),
                new HashMap<>(),
                null
        );
        customizer.customize(builder);
        return builder;
    }


    @Primary
    //@BatchDataSource
    @Bean(name = "representantDataSource")
    public DataSource representantDataSource(
                @Qualifier("representantDataSourceProperties") DataSourceProperties properties,
                @Value("${spring.datasource.representant.url}") String url,
                @Value("${spring.datasource.representant.username}") String username,
                @Value("${spring.datasource.representant.password}") String password
    ){
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setJdbcUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            dataSource.setDriverClassName("org.postgresql.Driver");
            return dataSource;
    }

//    @Bean(name = "dataSource")
//    @Primary
//    public DataSource dataSource() {
//        return representantDataSource();
//    }

    //
//    @Primary
//    @Bean(name = "representantEntityManager")
//    public LocalContainerEntityManagerFactoryBean entityManager() {
//        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(representantDataSource());
//        em.setPackagesToScan("com.emura_group.schema_based_tenant_management.domain.model");
//
//        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        em.setJpaVendorAdapter(vendorAdapter);
//        final HashMap<String, Object> properties = new HashMap<String, Object>();
//        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
//        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
//        em.setJpaPropertyMap(properties);
//
//        return em;
//    }
//    @Primary
//    @BatchDataSource
//    @Bean(name = "representantDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.representant")
//    public DataSource representantDataSource() {
//        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(Preconditions.checkNotNull("org.postgresql.Driver"));
//        dataSource.setUrl(Preconditions.checkNotNull("jdbc:postgresql://localhost:5432/em_db?currentSchema=assiganto"));
//        dataSource.setUsername(Preconditions.checkNotNull("postgres"));
//        dataSource.setPassword(Preconditions.checkNotNull("@Nathalie2024"));
//
//        return dataSource;
//    }



    @Primary
    @Bean(name = "representantJdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("representantDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Primary
    @Bean(name = "representantEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean representantEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                                   @Qualifier("representantDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.emura_group.schema_based_tenant_management.domain.model")
                .persistenceUnit("representant")
                .build();
    }

    @Primary
    @Bean(name = {"representantEntityManager", "entityManager"})
    public EntityManager representantEntityManager(
            @Qualifier("representantEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    @Primary
    @Bean(name = {"transactionManager", "representantTransactionManager"})
    public PlatformTransactionManager representantTransactionManager(
            @Qualifier("representantDataSource") DataSource dataSource) {
        //final JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
        //transactionManager.setEntityManagerFactory(entityManager().getObject());
        //return transactionManager;
        //return new JpaTransactionManager(entityManagerFactory);

        return new DataSourceTransactionManager(dataSource);
    }

//    @Bean(name = "representantPagingItemReader")
//    public JdbcPagingItemReader<Representant> pagingItemReader(@Qualifier("representantDataSource") DataSource dataSource) {
//        JdbcPagingItemReader<Representant> reader = new JdbcPagingItemReader<>();
//        reader.setDataSource(dataSource);
//        reader.setFetchSize(10); // Adjust page size as needed
//        reader.setRowMapper(new BeanPropertyRowMapper<>(Representant.class));
//
//        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
//        queryProvider.setSelectClause("id, country, companyName");
//        queryProvider.setFromClause("from representant");
//        queryProvider.setSortKeys(Collections.singletonMap("id", Order.ASCENDING));
//
//        reader.setQueryProvider(queryProvider);
//        return reader;
//    }


    @Bean(name = "representantJobRepository")
    public JobRepository jobRepository(@Qualifier("representantDataSource") DataSource dataSource,
                                       @Qualifier("representantTransactionManager") PlatformTransactionManager transactionManager)
            throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.setDatabaseType(DatabaseType.POSTGRES.name());
        factory.setIsolationLevelForCreate("ISOLATION_SERIALIZABLE");
        factory.setTablePrefix("BATCH_");
        factory.afterPropertiesSet();
        return factory.getObject();
    }
}
