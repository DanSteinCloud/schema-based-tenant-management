package com.emura_group.schema_based_tenant_management.datasource.batch;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.google.common.base.Preconditions;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@PropertySource({"classpath:application-tenant.properties"})
@EnableJpaRepositories(basePackages = "com.emura_group.schema_based_tenant_management.domain.entity", entityManagerFactoryRef = "tenantEntityManager", transactionManagerRef = "tenantTransactionManager")
@Profile("!tc")
@EnableBatchProcessing
public class PersistenceTenantConfiguration {
    @Autowired
    private Environment env;

    public PersistenceTenantConfiguration() {
        super();
    }

    //

    //@Primary
    @Bean(name = "tenantEntityManager")
    public LocalContainerEntityManagerFactoryBean tenantEntityManager() {
        System.out.println("env config value" + env.getProperty("jdbc.user"));
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(tenantDataSource());
        em.setPackagesToScan("com.emura_group.schema_based_tenant_management.domain.entity");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    //@Primary
    @Bean(name = "tenantDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.tenant")
    public DataSource tenantDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Preconditions.checkNotNull("org.postgresql.Driver"));
        dataSource.setUrl(Preconditions.checkNotNull("jdbc:postgresql://localhost:5432/assiganto_system"));
        dataSource.setUsername(Preconditions.checkNotNull("postgres"));
        dataSource.setPassword(Preconditions.checkNotNull("@Nathalie2024"));

        return dataSource;
    }

    @Bean(name = "tenantJdbcTemplate")
    //@Primary
    public JdbcTemplate jdbcTenantTemplate(@Qualifier("tenantDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    //@Primary
    @Bean(name = "tenantTransactionManager")
    public PlatformTransactionManager tenantTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(tenantEntityManager().getObject());
        return transactionManager;
    }

}
