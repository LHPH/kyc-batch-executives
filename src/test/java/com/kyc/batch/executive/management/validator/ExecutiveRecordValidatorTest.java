package com.kyc.batch.executive.management.validator;

import com.kyc.batch.executive.management.entity.KycExecutive;
import com.kyc.batch.executive.management.enums.BatchExecutiveProcessEnum;
import com.kyc.batch.executive.management.model.ExecutiveRawData;
import com.kyc.batch.executive.management.repository.KycExecutiveRepository;
import com.kyc.core.exception.KycBatchException;
import com.kyc.core.model.web.MessageData;
import com.kyc.core.properties.KycMessages;
import com.kyc.core.validation.engine.ValidationRuleEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ExecutiveRecordValidatorTest {

    @Mock
    private KycExecutiveRepository kycExecutiveRepository;

    @Spy
    private ValidationRuleEngine validationRuleEngine;

    @Mock
    private KycMessages kycMessages;

    @InjectMocks
    private ExecutiveRecordValidator validator;

    @BeforeAll
    public static void init(){
        MockitoAnnotations.openMocks(ExecutiveRecordValidatorTest.class);
    }

    @Test
    public void validate_validatingRegistrationData_successfulValidation(){

        ExecutiveRawData record = new ExecutiveRawData();
        record.setOperation(BatchExecutiveProcessEnum.REGISTRATION.getOperation());
        record.setFirstName("TEST");
        record.setLastName("TEST");
        record.setRfc("TEST901010KI9");
        record.setPhone("5544332211");
        record.setEmail("a@a.com");
        record.setIdBranch(1);

        Assertions.assertDoesNotThrow(()-> { validator.validate(record); });
    }

    @Test
    public void validate_validatingRegistrationDataWithBadData_throwException(){

        ExecutiveRawData record = new ExecutiveRawData();
        record.setOperation(BatchExecutiveProcessEnum.REGISTRATION.getOperation());
        record.setFirstName("");
        record.setLastName("");
        record.setRfc("TEST901010KI9");
        record.setPhone("5544332");
        record.setEmail("a@a.com");
        record.setIdBranch(1);

        given(kycMessages.getMessage(anyString())).willReturn(new MessageData());

        Assertions.assertThrows(KycBatchException.class,()-> { validator.validate(record); });
    }

    @Test
    public void validate_validatingUpdatingData_successfulValidation(){

        ExecutiveRawData record = new ExecutiveRawData();
        record.setOperation(BatchExecutiveProcessEnum.MODIFICATION.getOperation());
        record.setExecutiveNumber(1L);
        record.setFirstName("TEST");
        record.setLastName("TEST");
        record.setRfc("TEST901010KI9");
        record.setPhone("5544332211");
        record.setEmail("a@a.com");
        record.setIdBranch(1);

        given(kycExecutiveRepository.findById(anyLong())).willReturn(Optional.of(new KycExecutive()));

        Assertions.assertDoesNotThrow(()-> { validator.validate(record); });
    }

    @Test
    public void validate_validatingDisableExecutive_successfulValidation(){

        ExecutiveRawData record = new ExecutiveRawData();
        record.setOperation(BatchExecutiveProcessEnum.DISABLING.getOperation());
        record.setExecutiveNumber(1L);
        given(kycExecutiveRepository.findById(anyLong())).willReturn(Optional.of(new KycExecutive()));

        Assertions.assertDoesNotThrow(()-> { validator.validate(record); });
    }

    @Test
    public void validate_validatingDisableExecutiveButExecutiveDoesNotExists_throwsException(){

        ExecutiveRawData record = new ExecutiveRawData();
        record.setOperation(BatchExecutiveProcessEnum.DISABLING.getOperation());
        record.setExecutiveNumber(1L);

        given(kycExecutiveRepository.findById(anyLong())).willReturn(Optional.empty());
        given(kycMessages.getMessage(anyString())).willReturn(new MessageData());

        Assertions.assertThrows(KycBatchException.class,()-> { validator.validate(record); });
    }

    @Test
    public void validate_unknownOperation_throwException(){

        ExecutiveRawData record = new ExecutiveRawData();
        record.setOperation(100);
        record.setFirstName("TEST");
        record.setLastName("TEST");
        record.setRfc("TEST901010KI9");
        record.setPhone("5544332211");
        record.setEmail("a@a.com");
        record.setIdBranch(1);

        given(kycMessages.getMessage(anyString())).willReturn(new MessageData());

        Assertions.assertThrows(KycBatchException.class,()-> { validator.validate(record); });
    }

}
