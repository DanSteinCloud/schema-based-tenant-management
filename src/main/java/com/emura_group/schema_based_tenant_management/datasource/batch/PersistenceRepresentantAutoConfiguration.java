package com.emura_group.schema_based_tenant_management.datasource.batch;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * By default, the application-batch.properties file is read for
 * non auto configuration in PersistenceProductConfiguration.
 * <p>
 * If we need to use persistence-multiple-db-boot.properties and auto configuration
 * then uncomment the below @Configuration class and comment out PersistenceProductConfiguration.
 */
//@Configuration
//@EnableAspectJAutoProxy(proxyTargetClass = true)
@PropertySource({"classpath:persistence-multiple-db-boot.properties"})
@EnableJpaRepositories(basePackages = "com.emura_group.schema_based_tenant_management.domain.model", entityManagerFactoryRef = "entityManager", transactionManagerRef = "transactionManager")
//@Profile("!tc")
@Component
@EnableBatchProcessing
public class PersistenceRepresentantAutoConfiguration {
    @Autowired
    private Environment env;

    public PersistenceRepresentantAutoConfiguration() {
        super();
    }

    //

    @Bean
    public LocalContainerEntityManagerFactoryBean representantEntityManager(DataSource representantDataSource) {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(representantDataSource);
        em.setPackagesToScan("com.emura_group.schema_based_tenant_management.domain.model");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    @ConfigurationProperties(prefix="spring.second-datasource")
    public DataSource representantDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager representantTransactionManager(LocalContainerEntityManagerFactoryBean representantEntityManager) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(representantEntityManager.getObject());
        return transactionManager;
    }

}
