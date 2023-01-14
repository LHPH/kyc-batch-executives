package com.kyc.batchs.executive.management.config;

import com.kyc.batchs.commons.configuration.DefaultConfigurationBatch;
import com.kyc.batchs.commons.configuration.JpaPostgresqlConfig;
import com.kyc.batchs.executive.management.listener.ExecutiveJobManagementListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableBatchProcessing
@Import(value = {JpaPostgresqlConfig.class, DefaultConfigurationBatch.class})
@EntityScan(basePackages = "com.kyc.batchs.executive.management.entity")
public class KycExecutiveManagementBatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job executiveManagementJob(Step executiveManagementStep, Step userManagementStep){
        return jobBuilderFactory.get("executiveManagementJob")
                .incrementer(new RunIdIncrementer())
                .listener(executiveJobManagementListener())
                .flow(executiveManagementStep)
                .next(userManagementStep)
                .end()
                .build();
    }

    @Bean
    public ExecutiveJobManagementListener executiveJobManagementListener(){
        return new ExecutiveJobManagementListener();
    }

}
