package com.kyc.batchs.executive.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.kyc.batchs.executive.management"})
public class KycExecutiveManagementBatch {


    public static void main(String[] args) {
        SpringApplication.run(KycExecutiveManagementBatch.class, args);
    }
}
