package com.backend.ecommercebackend.config;


import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.backend.ecommercebackend.repository.order",
        entityManagerFactoryRef = "thirdEntityManagerFactory",
        transactionManagerRef = "thirdTransactionManager"
)

public class OrderDbConfig {

    @Value("${spring.datasource.third.url}")
    private String thirdDbUrl;

    @Value("${spring.datasource.third.username}")
    private String thirdDbUsername;

    @Value("${spring.datasource.third.password}")
    private String thirdDbPassword;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String thirdDbDdlAuto;


    @Bean(name = "thirdDataSource")
    public DataSource thirdDataSource() {
        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        builder.url(thirdDbUrl);
        builder.username(thirdDbUsername);
        builder.password(thirdDbPassword);
        return builder.build();
    }


    @Bean(name = "thirdEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean thirdEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(thirdDataSource())
                .packages("com.backend.ecommercebackend.model.order")
                .persistenceUnit("third")
                .properties(hibernateProperties())
                .build();
    }

    @Bean(name = "thirdTransactionManager")
    public PlatformTransactionManager thirdTransactionManager(
            @Qualifier("thirdEntityManagerFactory") EntityManagerFactory thirdEntityManagerFactory) {
        return new JpaTransactionManager(thirdEntityManagerFactory);
    }

    private Map<String, Object> hibernateProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", thirdDbDdlAuto);
        return properties;
    }
}