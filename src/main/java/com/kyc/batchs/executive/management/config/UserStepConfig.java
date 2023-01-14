package com.kyc.batchs.executive.management.config;

import com.kyc.batchs.executive.management.classifier.UserExecutiveClassifier;
import com.kyc.batchs.executive.management.entity.KycExecutive;
import com.kyc.batchs.executive.management.model.ProcessExecutiveRecord;
import com.kyc.batchs.executive.management.processor.UserItemProcessor;
import com.kyc.batchs.executive.management.repository.KycExecutiveRepository;
import com.kyc.batchs.executive.management.repository.KycUserRepository;
import com.kyc.batchs.executive.management.writer.UpdatingUserItemWriter;
import com.kyc.batchs.executive.management.writer.RegistrationUserItemWriter;
import com.kyc.core.services.PasswordEncoderService;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class UserStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("kycTransactionManager")
    private PlatformTransactionManager kycEntityManagerFactory;

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    private KycExecutiveRepository kycExecutiveRepository;

    @Autowired
    private KycUserRepository kycUserRepository;

    @Bean
    public Step userManagementStep(){

        return stepBuilderFactory
                .get("userManagementStep")
                .transactionManager(kycEntityManagerFactory)
                .<KycExecutive, ProcessExecutiveRecord>chunk(10)
                .reader(databaseExecutiveItemReader())
                .processor(userExecutiveItemProcessor())
                .writer(classifierCompositeItemWriter())
                .faultTolerant()
                .build();
    }

    @Bean
    public RepositoryItemReader<KycExecutive> databaseExecutiveItemReader(){

        RepositoryItemReader<KycExecutive> reader = new RepositoryItemReader<>();
        reader.setPageSize(10);
        reader.setRepository(kycExecutiveRepository);
        reader.setMethodName("getExecutivesToProcess");
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        reader.setSort(sorts);
        return reader;
    }

    @Bean
    public UserItemProcessor userExecutiveItemProcessor (){
        return new UserItemProcessor(kycUserRepository,passwordEncoderService());
    }

    @Bean
    public RegistrationUserItemWriter registrationUserItemWriter(){
        return new RegistrationUserItemWriter(kycExecutiveRepository,kycUserRepository);
    }

    @Bean
    public UpdatingUserItemWriter updatingUserItemWriter(){
        return new UpdatingUserItemWriter(kycExecutiveRepository,kycUserRepository);
    }

    @Bean
    public ClassifierCompositeItemWriter classifierCompositeItemWriter(){
        ClassifierCompositeItemWriter classifierCompositeItemWriter = new ClassifierCompositeItemWriter();
        classifierCompositeItemWriter.setClassifier(new UserExecutiveClassifier(registrationUserItemWriter(),updatingUserItemWriter()));
        return classifierCompositeItemWriter;
    }

    @Bean
    public PasswordEncoderService passwordEncoderService(){
        return new PasswordEncoderService();
    }
}
