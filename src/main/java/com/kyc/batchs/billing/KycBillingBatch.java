package com.kyc.batchs.billing;

import com.kyc.batchs.payment.KycPaymentBatch;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.kyc.batchs.billing"})
public class KycBillingBatch {
    public static void main(String[] args) {
        SpringApplication.run(KycBillingBatch.class, args);
    }
}
