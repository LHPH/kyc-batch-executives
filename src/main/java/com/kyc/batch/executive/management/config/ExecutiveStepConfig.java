package com.kyc.batch.executive.management.config;

import com.kyc.batch.executive.management.entity.KycExecutive;
import com.kyc.batch.executive.management.mappers.ExecutiveMapper;
import com.kyc.batch.executive.management.model.ExecutiveRawData;
import com.kyc.batch.executive.management.processor.ExecutiveItemProcessor;
import com.kyc.batch.executive.management.repository.KycExecutiveRepository;
import com.kyc.batch.executive.management.validator.ExecutiveRecordValidator;
import com.kyc.core.batch.BatchStepListener;
import com.kyc.core.batch.BatchValidatingItemProcessor;
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

    private static final String STEP_NAME = "KYC-BATCH-EXECUTIVE-STEP-MNG-EXECUTIVES";

    @Bean
    public Step executiveManagementStep(){
        return stepBuilderFactory
                .get(STEP_NAME)
                .listener(executiveBatchStepListener())
                .<ExecutiveRawData, KycExecutive>chunk(10)
                .reader(fileExecutiveItemReader())
                .processor(compositeItemProcessor())
                .writer(databaseExecutiveItemWriter())
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

        return new ExecutiveRecordValidator(kycExecutiveRepository,validationRuleEngine());
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
        return new BatchStepListener<>(STEP_NAME);
    }

}
