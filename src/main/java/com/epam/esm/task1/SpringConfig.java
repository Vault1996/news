package com.epam.esm.task1;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Configuration
@EnableTransactionManagement
@PropertySources({ @PropertySource("classpath:database.properties"), @PropertySource("classpath:hibernate.properties"),
		@PropertySource("classpath:newsXml.properties") })
public class SpringConfig {

	private static final int SCHEDULED_THREAD_POOL_SIZE = 1;
	private static final String EXECUTOR_CORE_POOL_SIZE_PROPERTY_NAME = "newsXml.corePoolSize";
	private static final int DEFAULT_EXECUTOR_CORE_POOL_SIZE = 10;

	private static final String DEFAULT_HIBERNATE_SHOW_SQL_PROPERTY = "true";
	private static final String HIBERANTE_SHOW_SQL_PROPERTY = "hib.show_sql";
	private static final String HIBERNATE_DIALECT_PROPERTY = "hib.dialect";
	private static final String BASE_ENTITY_PACKAGE = "com.epam.esm.task1.entity";
	private static final String DRIVER_CLASS_NAME = "datasource.driverClassName";
	private static final String DATABASE_URL = "datasource.url";
	private static final String USER = "datasource.user";
	private static final String PASSWORD = "datasource.password";

	@Autowired
	private Environment env;

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean(destroyMethod = "close")
	public DataSource dbcp2BasicDataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(env.getRequiredProperty(DRIVER_CLASS_NAME));
		ds.setUrl(env.getProperty(DATABASE_URL));
		ds.setUsername(env.getRequiredProperty(USER));
		ds.setPassword(env.getRequiredProperty(PASSWORD));
		return ds;
	}

	@Bean
	public ObjectMapper xmlMapper() {
		return new XmlMapper();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dbcp2BasicDataSource());
		em.setPackagesToScan(BASE_ENTITY_PACKAGE);

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(hibernateProperties());

		return em;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean
	public PersistenceUnitUtil persistenceUnitUtil(EntityManagerFactory emf) {
		return emf.getPersistenceUnitUtil();
	}

	@Bean
	public ScheduledExecutorService newsImportScheduledExecutorService() {
		return Executors.newScheduledThreadPool(SCHEDULED_THREAD_POOL_SIZE);
	}

	@Bean
	public ExecutorService newsImportExecutorService() {
		return Executors.newFixedThreadPool(
				env.getProperty(EXECUTOR_CORE_POOL_SIZE_PROPERTY_NAME, Integer.class, DEFAULT_EXECUTOR_CORE_POOL_SIZE));
	}

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		return new MethodValidationPostProcessor();
	}

	private Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put(org.hibernate.cfg.Environment.DIALECT, env.getRequiredProperty(HIBERNATE_DIALECT_PROPERTY));
		properties.put(org.hibernate.cfg.Environment.SHOW_SQL,
				env.getProperty(HIBERANTE_SHOW_SQL_PROPERTY, DEFAULT_HIBERNATE_SHOW_SQL_PROPERTY));
		return properties;
	}
}
