package com.kyc.batch.executive.management.config.job;

import com.kyc.core.batch.BatchJobExecutionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.kyc.batch.executive.management.constants.KycBatchExecutiveConstants.JOB_NAME;

@Configuration
public class ExecutiveManagementJobConfig {

    @Bean
    public Job executiveManagementJob(JobRepository jobRepository,
                                      Step executiveManagementStep,
                                      Step userManagementStep,
                                      Step cleanFileStep){

        return new JobBuilder(JOB_NAME,jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(executiveJobManagementListener())
                .start(executiveManagementStep)
                .next(userManagementStep)
                .next(cleanFileStep)
                .build();
    }

    @Bean
    public BatchJobExecutionListener executiveJobManagementListener(){

        return new BatchJobExecutionListener(JOB_NAME);
    }

}
