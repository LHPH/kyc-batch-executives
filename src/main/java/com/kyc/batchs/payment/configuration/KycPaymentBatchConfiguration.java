package com.kyc.batchs.payment.configuration;

import com.kyc.batchs.commons.configuration.DefaultConfigurationBatch;
import com.kyc.batchs.payment.steps.ProcessResultPaymentStep;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static com.kyc.batchs.commons.constants.KycBatchConstants.KYC_BATCH_PAYMENT_JOB;

@Configuration
@EnableBatchProcessing
@Import(DefaultConfigurationBatch.class)
public class KycPaymentBatchConfiguration {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private ProcessResultPaymentStep processResultPaymentStep;

    @Bean
    public Job kycPaymentJob(){
        return jobs.get(KYC_BATCH_PAYMENT_JOB)
                .incrementer(new RunIdIncrementer())
                .start(stepOne())
                //.next(stepTwo())
                .build();
    }

    @Bean
    public Step stepOne(){
        return processResultPaymentStep.getStep(KYC_BATCH_PAYMENT_JOB+"_");
    }


}
