package com.kyc.batch.executive.management.config.steps;

import com.kyc.batch.executive.management.classifier.UserExecutiveClassifier;
import com.kyc.batch.executive.management.entity.KycExecutive;
import com.kyc.batch.executive.management.model.ProcessExecutiveRecord;
import com.kyc.batch.executive.management.processor.UserItemProcessor;
import com.kyc.batch.executive.management.repository.KycExecutiveRepository;
import com.kyc.batch.executive.management.repository.KycUserRepository;
import com.kyc.batch.executive.management.writer.UpdatingUserItemWriter;
import com.kyc.batch.executive.management.writer.RegistrationUserItemWriter;
import com.kyc.core.batch.BatchStepListener;
import com.kyc.core.exception.handlers.KycBatchExceptionHandler;
import com.kyc.core.services.PasswordEncoderService;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;

import static com.kyc.batch.executive.management.constants.KycBatchExecutiveConstants.ADM_USERS_STEP;

@Configuration
public class UserStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private KycExecutiveRepository kycExecutiveRepository;

    @Autowired
    private KycUserRepository kycUserRepository;

    @Autowired
    private KycBatchExceptionHandler exceptionHandler;

    @Bean
    public Step userManagementStep(){

        return stepBuilderFactory
                .get(ADM_USERS_STEP)
                .listener(userBatchStepListener())
                .<KycExecutive, ProcessExecutiveRecord>chunk(10)
                .reader(databaseExecutiveItemReader())
                .processor(userExecutiveItemProcessor())
                .writer(classifierCompositeItemWriter())
                .exceptionHandler(exceptionHandler)
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
    public BatchStepListener<KycExecutive, ProcessExecutiveRecord> userBatchStepListener(){
        return new BatchStepListener<>(ADM_USERS_STEP);
    }
}
