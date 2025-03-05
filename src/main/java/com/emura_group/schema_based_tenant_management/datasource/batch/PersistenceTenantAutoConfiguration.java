package com.emura_group.schema_based_tenant_management.datasource.batch;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * By default, the application-batch.properties file is read for
 * non auto configuration in PersistenceUserConfiguration.
 * <p>
 * If we need to use persistence-multiple-db-boot.properties and auto configuration
 * then uncomment the below @Configuration class and comment out PersistenceUserConfiguration.
 */
//@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@PropertySource({"classpath:persistence-multiple-db-boot.properties"})
@EnableJpaRepositories(basePackages = "com.emura_group.schema_based_tenant_management.domain.entity.tenant", entityManagerFactoryRef = "tenantEntityManager", transactionManagerRef = "tenantTransactionManager")
@Profile("!tc")
@Component
@EnableBatchProcessing
public class PersistenceTenantAutoConfiguration {
    @Autowired
    private Environment env;

    public PersistenceTenantAutoConfiguration() {
        super();
    }

    //

   // @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean tenantEntityManager(DataSource tenantDataSource) {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(tenantDataSource);
        em.setPackagesToScan("com.emura_group.schema_based_tenant_management.domain.entity.tenant");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
   // @Primary
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource tenantDataSource() {
        return DataSourceBuilder.create().build();
    }

   // @Primary
    @Bean
    public PlatformTransactionManager tenantTransactionManager(LocalContainerEntityManagerFactoryBean tenantEntityManager) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(tenantEntityManager.getObject());
        return transactionManager;
    }

}
