package com.kyc.batchs.commons.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "kycEntityManagerFactory",
        transactionManagerRef = "kycTransactionManager",
        basePackages = {
                "com.kyc.batchs.commons.entity",
                "com.kyc.batchs.executive.management.entity",
                "com.kyc.batchs.executive.management.repository"
        }
)
@Import(DataSourceConfig.class)
public class JpaPostgresqlConfig {

    private String [] entityBasePackages ={
            "com.kyc.batchs.commons.entity",
            "com.kyc.batchs.executive.management.entity"
    };

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean kycEntityManagerFactory(@Qualifier("kycDataSource")DataSource dataSource,
    EntityManagerFactoryBuilder builder) {

        LocalContainerEntityManagerFactoryBean localContainer =  builder
                .dataSource(dataSource)
                .packages(entityBasePackages)
                .build();

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
        properties.put("hibernate.show_sql", "true");

        localContainer.setJpaVendorAdapter(vendorAdapter);
        localContainer.setJpaPropertyMap(properties);

        return localContainer;
    }

    @Primary
    @Bean(name = "kycTransactionManager")
    public PlatformTransactionManager kycTransactionManager(
            @Qualifier("kycEntityManagerFactory") EntityManagerFactory customerEntityManagerFactory) {
        return new JpaTransactionManager(customerEntityManagerFactory);
    }



}
