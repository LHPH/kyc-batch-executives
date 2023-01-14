package com.kyc.batchs.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.StepListener;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.StepListenerSupport;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.repeat.exception.ExceptionHandler;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;


public class BaseStep<I,O> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private String nameStep;
    private Integer chunkSize;
    private ItemProcessor<I,O> processor;
    private ItemReader<I> reader;
    private List<ItemWriter<? super O>> writers = new ArrayList<>();
    private CompositeItemWriter<O> compositeWriter = new CompositeItemWriter<>();
    private List<StepListener> listeners = new ArrayList<>();

    private ExceptionHandler exceptionHandler;

    @SuppressWarnings("unchecked")
    public Step buildStep(StepBuilderFactory factory){

        StepBuilder builder = factory.get(getNameStep());
        SimpleStepBuilder<I,O> stepBuilder = builder
                .<I,O>chunk(getChunkSize())
                .reader(getReader())
                .processor(getProcessor())
                .writer(getWriter())
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(10);

        for(StepListener listener : getListeners()){
            if(listener instanceof BaseListenerSupport){
                stepBuilder = stepBuilder.listener(((BaseListenerSupport<I,O>)listener).getListenerSupport());
                LOGGER.debug("Agregando listener al step");
            }
            else{
                stepBuilder = stepBuilder.listener(listener);
            }

        }

        return stepBuilder.build();
    }

    public String getNameStep() {
        return nameStep;
    }

    public void setNameStep(String nameStep) {
        this.nameStep = nameStep;
    }

    public Integer getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(Integer chunkSize) {
        this.chunkSize = chunkSize;
    }

    public ItemProcessor<? super I,? extends O> getProcessor() {
        return processor;
    }

    public void setProcessor(ItemProcessor<I, O> processor) {
        this.processor = processor;
    }

    public ItemReader<I> getReader() {
        return reader;
    }

    public void setReader(ItemReader<I> reader) {
        this.reader = reader;
    }

    public void addWriter(ItemWriter<? super O> writer){
        writers.add(writer);
    }

    public CompositeItemWriter<O> getWriter(){

        compositeWriter.setDelegates(writers);
        return compositeWriter;
    }

    public void addListener(StepListener listener){
        this.listeners.add(listener);
    }

    public List<StepListener> getListeners() {
        return listeners;
    }

    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }
}
