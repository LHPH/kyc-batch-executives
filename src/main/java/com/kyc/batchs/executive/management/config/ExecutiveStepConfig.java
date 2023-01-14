package com.kyc.batchs.executive.management.config;

import com.kyc.batchs.executive.management.entity.KycExecutive;
import com.kyc.batchs.executive.management.mappers.ExecutiveMapper;
import com.kyc.batchs.executive.management.model.ExecutiveRawData;
import com.kyc.batchs.executive.management.processor.ExecutiveItemProcessor;
import com.kyc.batchs.executive.management.repository.KycExecutiveRepository;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;

@Configuration
public class ExecutiveStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    @Qualifier("kycTransactionManager")
    private PlatformTransactionManager kycEntityManagerFactory;

    @Value("${kyc.batch.executive-management.path}")
    private String filePath;

    @Value("${kyc.batch.executive-management.fields}")
    private String fields;

    @Autowired
    private ExecutiveMapper executiveMapper;

    @Autowired
    private KycExecutiveRepository kycExecutiveRepository;

    @Bean
    public Step executiveManagementStep(){
        return stepBuilderFactory
                .get("executiveManagementStep")
                .transactionManager(kycEntityManagerFactory)
                .<ExecutiveRawData, KycExecutive>chunk(10)
                .reader(fileExecutiveItemReader())
                .processor(executiveItemProcessor())
                .writer(databaseExecutiveItemWriter())
                .faultTolerant()

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
    public ExecutiveItemProcessor executiveItemProcessor(){
        return new ExecutiveItemProcessor(executiveMapper,kycExecutiveRepository);
    }

    @Bean
    public JpaItemWriter<KycExecutive> databaseExecutiveItemWriter(){
        JpaItemWriter<KycExecutive> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(emf);
        return jpaItemWriter;
    }
}
