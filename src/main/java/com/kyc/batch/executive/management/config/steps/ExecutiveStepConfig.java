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
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import javax.persistence.EntityManagerFactory;
import java.util.Arrays;

import static com.kyc.batch.executive.management.constants.KycBatchExecutiveConstants.ADM_EXECUTIVE_STEP;

@Configuration
public class ExecutiveStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private EntityManagerFactory emf;

    @Value("${kyc.batch.executive-management.path}")
    private String filePath;

    @Value("${kyc.batch.executive-management.fields}")
    private String fields;

    @Autowired
    private ExecutiveMapper executiveMapper;

    @Autowired
    private KycExecutiveRepository kycExecutiveRepository;

    @Autowired
    private KycBatchExceptionHandler exceptionHandler;

    @Autowired
    private KycMessages kycMessages;

    @Bean
    public Step executiveManagementStep(){
        return stepBuilderFactory
                .get(ADM_EXECUTIVE_STEP)
                .listener(executiveBatchStepListener())
                .<ExecutiveRawData, KycExecutive>chunk(10)
                .reader(fileExecutiveItemReader())
                .processor(compositeItemProcessor())
                .writer(databaseExecutiveItemWriter())
                .exceptionHandler(exceptionHandler)
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
        JpaItemWriter<KycExecutive> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(emf);
        return jpaItemWriter;
    }

    @Bean
    public BatchStepListener<ExecutiveRawData, KycExecutive> executiveBatchStepListener(){
        return new BatchStepListener<>(ADM_EXECUTIVE_STEP);
    }
}
