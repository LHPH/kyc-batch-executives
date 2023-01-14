package com.kyc.batchs.charging;

import com.kyc.batchs.billing.KycBillingBatch;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(scanBasePackages = {"com.kyc.batchs.charging"})
public class KycChargingBatch {
    public static void main(String[] args) {
        SpringApplication.run(KycChargingBatch.class, args);
    }
}
