package com.kyc.batch.executive.management.processor;

import com.kyc.batch.executive.management.entity.KycExecutive;
import com.kyc.batch.executive.management.entity.KycUser;
import com.kyc.batch.executive.management.enums.BatchExecutiveProcessEnum;
import com.kyc.batch.executive.management.model.ProcessExecutiveRecord;
import com.kyc.batch.executive.management.repository.KycUserRepository;
import com.kyc.core.services.PasswordEncoderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserItemProcessorTest {

    @Mock
    private KycUserRepository kycUserRepository;

    @Mock
    private PasswordEncoderService passwordEncoderService;

    @InjectMocks
    private UserItemProcessor userItemProcessor;

    @BeforeAll
    public static void init(){
        MockitoAnnotations.openMocks(UserItemProcessorTest.class);
    }

    @Test
    public void process_processingRegistration_returnEntityToRegister(){

        KycExecutive kycExecutive = new KycExecutive();
        kycExecutive.setBatchOperationControl(BatchExecutiveProcessEnum.REGISTRATION.getOperation());
        kycExecutive.setId(1L);

        ProcessExecutiveRecord result = userItemProcessor.process(kycExecutive);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(BatchExecutiveProcessEnum.REGISTRATION.getOperation(),result.getOperation().getOperation());

    }

    @Test
    public void process_processingDisabling_returnEntityToDisable(){

        KycExecutive kycExecutive = new KycExecutive();
        kycExecutive.setBatchOperationControl(BatchExecutiveProcessEnum.DISABLING.getOperation());
        kycExecutive.setIdUser(1L);

        given(kycUserRepository.getReferenceById(anyLong())).willReturn(new KycUser());

        ProcessExecutiveRecord result = userItemProcessor.process(kycExecutive);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(BatchExecutiveProcessEnum.DISABLING.getOperation(),result.getOperation().getOperation());
    }

    @Test
    public void process_processingEnabling_returnEntityToEnabling(){

        KycExecutive kycExecutive = new KycExecutive();
        kycExecutive.setBatchOperationControl(BatchExecutiveProcessEnum.ENABLING.getOperation());
        kycExecutive.setIdUser(1L);

        given(kycUserRepository.getReferenceById(anyLong())).willReturn(new KycUser());

        ProcessExecutiveRecord result = userItemProcessor.process(kycExecutive);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(BatchExecutiveProcessEnum.ENABLING.getOperation(),result.getOperation().getOperation());
    }

    @Test
    public void process_processingOtherCase_returnNull(){

        KycExecutive kycExecutive = new KycExecutive();
        kycExecutive.setBatchOperationControl(100);

        ProcessExecutiveRecord result = userItemProcessor.process(kycExecutive);
        Assertions.assertNull(result);
    }

}
