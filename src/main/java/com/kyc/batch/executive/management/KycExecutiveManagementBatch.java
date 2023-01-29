package com.kyc.batch.executive.management;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class KycExecutiveManagementBatch {

    public static void main(String[] args) {
        SpringApplication.run(KycExecutiveManagementBatch.class, args);
    }
}
