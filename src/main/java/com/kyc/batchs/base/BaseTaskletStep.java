package com.kyc.batchs.base;

import javafx.concurrent.Task;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.lang.invoke.MethodHandles;

@Setter
@Getter
public abstract class BaseTaskletStep {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private String taskletName;
    private Tasklet tasklet;

    public Step buildStep(StepBuilderFactory factory){
        return factory.get(getTaskletName())
                .tasklet(getTasklet())
                .build();
    }

}
