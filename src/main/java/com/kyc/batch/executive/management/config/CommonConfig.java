package com.kyc.batch.executive.management.config;

import com.kyc.core.persistence.repositories.KycParameterRepository;
import com.kyc.core.properties.KycMessages;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Import(value = {KycMessages.class})
@Configuration
@EnableJpaRepositories(basePackages = {"com.kyc.batch.executive.management.repository",
        "com.kyc.core.persistence.repositories"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = KycParameterRepository.class)})
@EntityScan(basePackages = {"com.kyc.core.persistence.entity","com.kyc.batch.executive.management.entity"})
public class CommonConfig {

}
