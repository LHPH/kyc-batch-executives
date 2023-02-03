package com.kyc.batch.executive.management.processor;

import com.kyc.batch.executive.management.entity.KycExecutive;
import com.kyc.batch.executive.management.enums.BatchExecutiveProcessEnum;
import com.kyc.batch.executive.management.mappers.ExecutiveMapper;
import com.kyc.batch.executive.management.model.ExecutiveRawData;
import com.kyc.batch.executive.management.repository.KycExecutiveRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ExecutiveItemProcessorTest {

    @Mock
    private ExecutiveMapper executiveMapper;

    @Mock
    private KycExecutiveRepository kycExecutiveRepository;

    @InjectMocks
    private ExecutiveItemProcessor executiveItemProcessor;

    @BeforeAll
    public static void init(){
        MockitoAnnotations.openMocks(ExecutiveItemProcessor.class);
    }

    @Test
    public void process_registeringExecutive_returnEntityToRegister(){

        ExecutiveRawData record = new ExecutiveRawData();
        record.setOperation(BatchExecutiveProcessEnum.REGISTRATION.getOperation());

        executiveItemProcessor.process(record);
        verify(executiveMapper,times(1)).toNewKycExecutive(any(ExecutiveRawData.class));
    }

    @Test
    public void process_updatingExecutive_returnEntityToUpdate(){

        ExecutiveRawData record = new ExecutiveRawData();
        record.setOperation(BatchExecutiveProcessEnum.MODIFICATION.getOperation());
        record.setExecutiveNumber(1L);

        given(kycExecutiveRepository.getReferenceById(anyLong())).willReturn(new KycExecutive());

        executiveItemProcessor.process(record);
        verify(executiveMapper,times(1)).updateKycExecutive(any(ExecutiveRawData.class),any(KycExecutive.class));
    }

    @Test
    public void process_disablingExecutive_returnEntityToDisabling(){

        ExecutiveRawData record = new ExecutiveRawData();
        record.setOperation(BatchExecutiveProcessEnum.DISABLING.getOperation());
        record.setExecutiveNumber(1L);

        given(kycExecutiveRepository.getReferenceById(anyLong())).willReturn(new KycExecutive());

        KycExecutive result = executiveItemProcessor.process(record);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(BatchExecutiveProcessEnum.DISABLING.getOperation(),result.getBatchOperationControl());
    }

    @Test
    public void process_enablingExecutive_returnEntityToEnabling(){

        ExecutiveRawData record = new ExecutiveRawData();
        record.setOperation(BatchExecutiveProcessEnum.ENABLING.getOperation());
        record.setExecutiveNumber(1L);

        given(kycExecutiveRepository.getReferenceById(anyLong())).willReturn(new KycExecutive());

        KycExecutive result = executiveItemProcessor.process(record);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(BatchExecutiveProcessEnum.ENABLING.getOperation(),result.getBatchOperationControl());
    }

    @Test
    public void process_otherCase_returnNull(){

        ExecutiveRawData record = new ExecutiveRawData();
        record.setOperation(100);

        Assertions.assertNull(executiveItemProcessor.process(record));
    }

}
