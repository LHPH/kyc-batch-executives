package com.kyc.batch.executive.management.config;

import com.kyc.batch.executive.management.tasklets.FileDeleteTasklet;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class CleanFileStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Value("${kyc.batch.executive-management.path}")
    private String filePath;

    private static final String TASKLET_NAME = "KYC-BATCH-EXECUTIVE-TASK-CLEAN-FILE";

    @Bean
    public Step cleanFileStep(){

        return stepBuilderFactory.get(TASKLET_NAME)
                .tasklet(fileDeleteTasklet())
                .build();
    }

    @Bean
    public Tasklet fileDeleteTasklet(){
        return new FileDeleteTasklet(new FileSystemResource(filePath));
    }
}
