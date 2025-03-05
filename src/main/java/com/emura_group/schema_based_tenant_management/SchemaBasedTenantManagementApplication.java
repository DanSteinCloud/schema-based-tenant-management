package com.emura_group.schema_based_tenant_management;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;

//@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class }, scanBasePackages = "com.emura_group.schema_based_tenant_management")
@EnableJpaRepositories("com.emura_group.schema_based_tenant_management.repository.batch")
@EnableBatchProcessing
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
//@ComponentScan(basePackages = "com.emura_group.schema_based_tenant_management.datasource.batch")
public class SchemaBasedTenantManagementApplication extends SpringBootServletInitializer {

	@Qualifier("representantDataSource")
	private final DataSource representantDataSource;

	@Qualifier("representantEntityManager")
	private final EntityManager entityManager;

	@Qualifier("representantEntityManagerFactory")
	private final EntityManagerFactory entityManagerFactory;

	@Qualifier("representantJobRepository")
	private final JobRepository jobRepository;

	@Qualifier("representantTransactionManager")
	private final PlatformTransactionManager transactionManager;

    public SchemaBasedTenantManagementApplication(
			@Qualifier("representantDataSource") DataSource representantDataSource,
			@Qualifier("representantEntityManagerFactory") EntityManagerFactory entityManagerFactory,
			@Qualifier("representantEntityManager") EntityManager entityManager,
			@Qualifier("representantTransactionManager") PlatformTransactionManager representantTransactionManager,
			@Qualifier("representantJobRepository") JobRepository jobRepository) {
		this.representantDataSource = representantDataSource;
		this.entityManager = entityManager;
		this.entityManagerFactory = entityManagerFactory;
		this.jobRepository = jobRepository;
		this.transactionManager = representantTransactionManager;
    }

    public static void main(String[] args) {
		SpringApplication.run(SchemaBasedTenantManagementApplication.class, args);
		System.out.println("Active profile: " + System.getProperty("spring.profiles.active"));
	}

	@Bean(name = "firstBatchJobLauncher")
	public JobLauncher jobLauncher() throws Exception {
		TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}

	@Bean
	public ApplicationRunner runner(ApplicationContext ctx) {
		return args -> {
			System.out.println("Available beans: " + Arrays.toString(ctx.getBeanDefinitionNames()));
		};
	}

	@Autowired
	private ApplicationContext applicationContext;

	@PostConstruct
	public void printBeans() {
		System.out.println("Beans available:");
		for (String beanName : applicationContext.getBeanDefinitionNames()) {
			System.out.println(beanName);
		}
	}


}
