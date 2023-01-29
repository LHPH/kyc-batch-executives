package com.kyc.batch.executive.management.config;

import com.kyc.core.batch.BatchJobExecutionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KycExecutiveManagementBatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    private static final String JOB_NAME = "KYC-BATCH-EXECUTIVE-JOB-MNG";

    @Bean
    public Job executiveManagementJob(Step executiveManagementStep, Step userManagementStep, Step cleanFileStep){
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(executiveJobManagementListener())
                .start(executiveManagementStep)
               // .next(cleanFileStep)
                //.next(userManagementStep)
                .build();
    }

    @Bean
    public BatchJobExecutionListener executiveJobManagementListener(){

        return new BatchJobExecutionListener(JOB_NAME);
    }

}
