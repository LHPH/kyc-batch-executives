package com.kyc.batchs.payment.steps;

import com.kyc.batchs.base.BaseStep;
import com.kyc.batchs.payment.listeners.PaymentListener;
import com.kyc.batchs.payment.model.BankResponse;
import com.kyc.batchs.payment.model.TransactionPayment;
import com.kyc.batchs.payment.processor.PaymentItemProcessor;
import com.kyc.batchs.payment.writers.PaymentItemWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import java.io.File;

@Configuration
public class ProcessResultPaymentStep extends BaseStep<BankResponse, TransactionPayment> {

    @Value("${kyc.batch.payments.path}")
    private String path;

    @Value("${kyc.batch.payments.file}")
    private String nameFile;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    public Step getStep(String name){

        this.setNameStep(name);
        this.setReader(flatFileItemReader());
        this.setProcessor(paymentItemProcessor());
        this.addListener(paymentListener());
        this.setChunkSize(10);

        return buildStep(stepBuilderFactory);
    }

    @Bean
    public FlatFileItemReader<BankResponse> flatFileItemReader(){

        return new FlatFileItemReaderBuilder<BankResponse>()
                .name("paymentFlatFileItemReader")
                .resource(new FileSystemResource(path+ File.separator+"SB"+File.separator+nameFile))
                .linesToSkip(1)
                .lineTokenizer(new DelimitedLineTokenizer(" "){{
                    setNames("id","dateStart","dateFinish","code","reference","comment");
                }})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<BankResponse>(){{
                    setTargetType(BankResponse.class);
                }})
                .build();
    }

    @Bean
    public PaymentItemProcessor paymentItemProcessor(){
        return new PaymentItemProcessor("SB");
    }

    @Bean
    public PaymentItemWriter paymentItemWriter(){

        return new PaymentItemWriter();
    }

    @Bean
    public PaymentListener paymentListener(){
        return new PaymentListener();
    }

}
