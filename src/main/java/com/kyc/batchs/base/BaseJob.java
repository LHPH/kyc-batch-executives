package com.kyc.batchs.base;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

import javax.batch.api.listener.JobListener;
import java.util.ArrayList;
import java.util.List;

public class BaseJob {

    private String nameJob;
    private List<Step> steps = new ArrayList<>();
    private JobListener jobListener;


    public Job getJob(JobBuilderFactory factory){

        JobBuilder jobBuilder = factory.get(getNameJob());
        SimpleJobBuilder builder = jobBuilder.start(steps.get(0));
        builder.listener(jobListener)
                .preventRestart()
                .incrementer(new RunIdIncrementer());

        for(Step step : getSteps()){
            builder.next(step);
        }
        return builder.build();
    }

    public String getNameJob() {
        return nameJob;
    }

    public void setNameJob(String nameJob) {
        this.nameJob = nameJob;
    }

    public void addStep(Step step){
        steps.add(step);
    }

    public List<Step> getSteps(){
        return this.steps;
    }

    public JobListener getJobListener() {
        return jobListener;
    }

    public void setJobListener(JobListener jobListener) {
        this.jobListener = jobListener;
    }
}
