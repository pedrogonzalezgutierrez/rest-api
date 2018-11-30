package com.kiesoft.customer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.kiesoft.customer.jpa.repository",
        entityManagerFactoryRef = "databaseEntityManager",
        transactionManagerRef = "databaseTransactionManager"
)
public class Database {

    @Autowired
    private Environment env;

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean databaseEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPackagesToScan("com.kiesoft.customer.jpa.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hikari.dataSource.url", env.getProperty("jdbc.database.url"));
        properties.put("hibernate.hikari.dataSource.user", env.getProperty("jdbc.database.username"));
        properties.put("hibernate.hikari.dataSource.password", env.getProperty("jdbc.database.password"));
        properties.put("hibernate.dialect", env.getProperty("jdbc.database.hibernate-dialect"));
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("jdbc.database.ddl-auto"));
        properties.put("hibernate.connection.provider_class", env.getProperty("jdbc.database.pool-provider"));
        properties.put("hibernate.hikari.dataSourceClassName", env.getProperty("jdbc.database.dataSourceClassName"));
        properties.put("hibernate.show_sql", env.getProperty("jdbc.database.show-sql"));
        properties.put("hibernate.format_sql", env.getProperty("jdbc.database.show-sql"));
        properties.put("hibernate.cache.use_second_level_cache", env.getProperty("jdbc.hibernate.cache.second-level"));
        properties.put("hibernate.cache.region.factory_class", env.getProperty("jdbc.hibernate.cache.region-factory-class"));
        properties.put("hibernate.jdbc.lob.non_contextual_creation", "true");

        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public PlatformTransactionManager databaseTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(databaseEntityManager().getObject());
        return transactionManager;
    }

    @Bean
    public DataSource userDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("jdbc.database.driver-class-name")));
        dataSource.setUrl(env.getProperty("jdbc.database.url"));
        dataSource.setUsername(env.getProperty("jdbc.database.username"));
        dataSource.setPassword(env.getProperty("jdbc.database.password"));

        return dataSource;
    }

}
