package com.kyc.batch.executive.management.config.steps;

import com.kyc.batch.executive.management.entity.KycExecutive;
import com.kyc.batch.executive.management.mappers.ExecutiveMapper;
import com.kyc.batch.executive.management.model.ExecutiveRawData;
import com.kyc.batch.executive.management.processor.ExecutiveItemProcessor;
import com.kyc.batch.executive.management.repository.KycExecutiveRepository;
import com.kyc.batch.executive.management.validator.ExecutiveRecordValidator;
import com.kyc.core.batch.BatchStepListener;
import com.kyc.core.batch.BatchValidatingItemProcessor;
import com.kyc.core.exception.handlers.KycBatchExceptionHandler;
import com.kyc.core.properties.KycMessages;
import com.kyc.core.validation.engine.ValidationRuleEngine;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.infrastructure.item.support.CompositeItemProcessor;
import org.springframework.batch.infrastructure.item.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;

import static com.kyc.batch.executive.management.constants.KycBatchExecutiveConstants.ADM_EXECUTIVE_STEP;

@Configuration
public class ExecutiveStepConfig {

    @Autowired
    private EntityManagerFactory emf;

    @Value("${kyc.batch.executive-management.path}")
    private String filePath;

    @Value("${kyc.batch.executive-management.chunk}")
    private Integer chunkSize;

    @Value("${kyc.batch.executive-management.fields}")
    private String fields;

    @Autowired
    private ExecutiveMapper executiveMapper;

    @Autowired
    private KycExecutiveRepository kycExecutiveRepository;

    @Autowired
    private KycMessages kycMessages;

    @Bean
    public Step executiveManagementStep(JobRepository jobRepository,
                                        PlatformTransactionManager platformTransactionManager){
        return new StepBuilder(ADM_EXECUTIVE_STEP,jobRepository)
                .listener(executiveBatchStepListener())
                .<ExecutiveRawData, KycExecutive>chunk(chunkSize)
                .reader(fileExecutiveItemReader())
                .processor(compositeItemProcessor())
                .writer(databaseExecutiveItemWriter())
                .transactionManager(platformTransactionManager)
                .build();
    }

    @Bean
    public FlatFileItemReader<ExecutiveRawData> fileExecutiveItemReader(){

        return new FlatFileItemReaderBuilder<ExecutiveRawData>()
                .name("executiveItemReader")
                .resource(new FileSystemResource(filePath))
                .delimited()
                .names(fields.split(","))
                .linesToSkip(1)
                .strict(false)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<ExecutiveRawData>(){{
                    setTargetType(ExecutiveRawData.class);
                }})
                .build();
    }

    @Bean
    public CompositeItemProcessor<ExecutiveRawData, KycExecutive> compositeItemProcessor(){
        CompositeItemProcessor<ExecutiveRawData, KycExecutive> compositeItemProcessor =
                new CompositeItemProcessor<>();
        compositeItemProcessor.setDelegates(Arrays.asList(beanValidatingItemProcessor(), executiveItemProcessor()));
        return compositeItemProcessor;
    }

    @Bean
    public BatchValidatingItemProcessor<ExecutiveRawData> beanValidatingItemProcessor(){
        BatchValidatingItemProcessor<ExecutiveRawData> processor = new BatchValidatingItemProcessor<>();
        processor.setFilter(false);
        processor.setValidator(executiveRecordValidator());
        return processor;
    }

    @Bean
    public Validator<ExecutiveRawData> executiveRecordValidator(){

        return new ExecutiveRecordValidator(kycExecutiveRepository,validationRuleEngine(),kycMessages);
    }

    @Bean
    public ValidationRuleEngine validationRuleEngine(){
        return new ValidationRuleEngine();
    }

    @Bean
    public ExecutiveItemProcessor executiveItemProcessor(){
        return new ExecutiveItemProcessor(executiveMapper,kycExecutiveRepository);
    }

    @Bean
    public JpaItemWriter<KycExecutive> databaseExecutiveItemWriter(){
        return new JpaItemWriter<>(emf);
    }

    @Bean
    public BatchStepListener<ExecutiveRawData, KycExecutive> executiveBatchStepListener(){
        return new BatchStepListener<>(ADM_EXECUTIVE_STEP,kycMessages.getMessage("001"));
    }
}
