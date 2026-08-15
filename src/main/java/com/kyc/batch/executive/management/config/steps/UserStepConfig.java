package com.kyc.batch.executive.management.config.steps;

import com.kyc.batch.executive.management.classifier.UserExecutiveClassifier;
import com.kyc.batch.executive.management.entity.KycExecutive;
import com.kyc.batch.executive.management.model.ProcessExecutiveRecord;
import com.kyc.batch.executive.management.processor.UserItemProcessor;
import com.kyc.batch.executive.management.repository.KycExecutiveRepository;
import com.kyc.batch.executive.management.writer.RegistrationUserItemWriter;
import com.kyc.batch.executive.management.writer.UpdatingUserItemWriter;
import com.kyc.core.batch.BatchStepListener;
import com.kyc.core.persistence.repositories.KycUserRepository;
import com.kyc.core.properties.KycMessages;
import com.kyc.core.services.PasswordEncoderService;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.data.RepositoryItemReader;
import org.springframework.batch.infrastructure.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

import static com.kyc.batch.executive.management.constants.KycBatchExecutiveConstants.ADM_USERS_STEP;

@Configuration
public class UserStepConfig {

    @Autowired
    private KycExecutiveRepository kycExecutiveRepository;

    @Autowired
    private KycUserRepository kycUserRepository;

    @Bean
    public Step userManagementStep(JobRepository jobRepository,
                                   PlatformTransactionManager platformTransactionManager,
                                   KycMessages kycMessages){

        return new StepBuilder(ADM_USERS_STEP,jobRepository)
                .listener(userBatchStepListener(kycMessages))
                .<KycExecutive, ProcessExecutiveRecord>chunk(10)
                .reader(databaseExecutiveItemReader())
                .processor(userExecutiveItemProcessor())
                .writer(classifierCompositeItemWriter())
                .transactionManager(platformTransactionManager)
                .build();
    }

    @Bean
    public RepositoryItemReader<KycExecutive> databaseExecutiveItemReader(){

        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);

        RepositoryItemReader<KycExecutive> reader = new RepositoryItemReader<>(kycExecutiveRepository,sorts);
        reader.setPageSize(10);
        reader.setMethodName("getExecutivesToProcess");

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
        return new UpdatingUserItemWriter(kycUserRepository);
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

    @Bean
    public BatchStepListener<KycExecutive, ProcessExecutiveRecord> userBatchStepListener(KycMessages kycMessages){
        return new BatchStepListener<>(ADM_USERS_STEP,kycMessages.getMessage("001"));
    }
}
