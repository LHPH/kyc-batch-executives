package com.kyc.batchs.executive.management.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

public class ExecutiveJobManagementListener extends JobExecutionListenerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutiveJobManagementListener.class);

    @Override
    public void afterJob(JobExecution jobExecution) {

        LOGGER.info("{}",jobExecution.getStatus());
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {

        LOGGER.info("{}",jobExecution.getStatus());

    }
}
