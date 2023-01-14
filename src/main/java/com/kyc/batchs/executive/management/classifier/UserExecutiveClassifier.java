package com.kyc.batchs.executive.management.classifier;

import com.kyc.batchs.executive.management.enums.ExecutiveMngEnum;
import com.kyc.batchs.executive.management.model.ProcessExecutiveRecord;
import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

@AllArgsConstructor
public class UserExecutiveClassifier implements Classifier<ProcessExecutiveRecord, ItemWriter<ProcessExecutiveRecord>> {


    private ItemWriter<ProcessExecutiveRecord> registerUserItemWriter;
    private ItemWriter<ProcessExecutiveRecord> updatingUserItemWriter;

    @Override
    public ItemWriter<ProcessExecutiveRecord> classify(ProcessExecutiveRecord processExecutiveRecord) {

        if(ExecutiveMngEnum.REGISTRATION.equals(processExecutiveRecord.getOperation())){
            return registerUserItemWriter;
        }
        else{
            return updatingUserItemWriter;
        }
    }
}
