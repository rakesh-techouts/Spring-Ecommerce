package com.techouts.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
@EnableTransactionManagement
public class HibernateConfig {

    private final Environment environment;

    public HibernateConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(environment.getProperty("app.datasource.driver", ""));
        ds.setUrl(environment.getProperty("app.datasource.url", ""));
        ds.setUsername(environment.getProperty("app.datasource.username", ""));
        ds.setPassword(environment.getProperty("app.datasource.password", ""));
        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource ds) {
        var vendor = new org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter();
        vendor.setShowSql(Boolean.parseBoolean(environment.getProperty("app.jpa.show-sql", "")));
        vendor.setGenerateDdl(true);

        var emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(ds);
        emf.setPackagesToScan("com.techouts.entity");
        emf.setJpaVendorAdapter(vendor);

        var props = new Properties();
        props.setProperty("hibernate.hbm2ddl.auto", environment.getProperty("app.jpa.hibernate.ddl-auto", ""));
        String dialect = environment.getProperty("app.jpa.hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        if (!dialect.isBlank()) {
            props.setProperty("hibernate.dialect", dialect);
        }
        props.setProperty("hibernate.format_sql", environment.getProperty("app.jpa.format-sql", ""));
        emf.setJpaProperties(props);
        return emf;
    }
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

}
