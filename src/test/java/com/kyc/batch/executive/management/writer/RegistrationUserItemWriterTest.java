package com.kyc.batch.executive.management.writer;

import com.kyc.batch.executive.management.entity.KycExecutive;
import com.kyc.batch.executive.management.entity.KycUser;
import com.kyc.batch.executive.management.model.ProcessExecutiveRecord;
import com.kyc.batch.executive.management.repository.KycExecutiveRepository;
import com.kyc.batch.executive.management.repository.KycUserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RegistrationUserItemWriterTest {

    @Mock
    private KycExecutiveRepository kycExecutiveRepository;

    @Mock
    private KycUserRepository kycUserRepository;

    @InjectMocks
    private RegistrationUserItemWriter registrationUserItemWriter;

    @BeforeAll
    public static void init(){
        MockitoAnnotations.openMocks(RegistrationUserItemWriterTest.class);
    }

    @Test
    public void write_writingRecords_recordsWereSaved(){

        ProcessExecutiveRecord record = new ProcessExecutiveRecord();
        record.setKycExecutive(new KycExecutive());
        record.setKycUser(new KycUser());

        given(kycUserRepository.save(any(KycUser.class)))
                .willReturn(new KycUser());

        registrationUserItemWriter.write(Collections.singletonList(record));
        verify(kycExecutiveRepository,times(1)).saveAndFlush(any(KycExecutive.class));
    }
}
