package com.kyc.batch.executive.management.classifier;

import com.kyc.batch.executive.management.enums.BatchExecutiveProcessEnum;
import com.kyc.batch.executive.management.model.ProcessExecutiveRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.ItemWriter;

@ExtendWith(MockitoExtension.class)
public class UserExecutiveClassifierTest {

    @Mock
    private ItemWriter<ProcessExecutiveRecord> registerUserItemWriter;

    @Mock
    private ItemWriter<ProcessExecutiveRecord> updatingUserItemWriter;

    private UserExecutiveClassifier userExecutiveClassifier;

    @BeforeAll
    public static void init(){
        MockitoAnnotations.openMocks(UserExecutiveClassifierTest.class);
    }

    @BeforeEach
    public void setUp(){
        userExecutiveClassifier = new UserExecutiveClassifier(registerUserItemWriter,updatingUserItemWriter);
    }

    @Test
    public void classify_registrationOperation_returnFirstItemWriter(){

        ProcessExecutiveRecord record = new ProcessExecutiveRecord();
        record.setOperation(BatchExecutiveProcessEnum.REGISTRATION);

        ItemWriter<ProcessExecutiveRecord> result = userExecutiveClassifier.classify(record);
        Assertions.assertEquals(registerUserItemWriter,result);
    }

    @Test
    public void classify_otherOperation_returnOtherItemWriter(){

        ProcessExecutiveRecord record = new ProcessExecutiveRecord();
        record.setOperation(BatchExecutiveProcessEnum.ENABLING);

        ItemWriter<ProcessExecutiveRecord> result = userExecutiveClassifier.classify(record);
        Assertions.assertEquals(updatingUserItemWriter,result);
    }
}
