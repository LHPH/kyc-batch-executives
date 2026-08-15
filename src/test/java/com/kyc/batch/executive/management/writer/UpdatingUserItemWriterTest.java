package com.kyc.batch.executive.management.writer;

import com.kyc.batch.executive.management.entity.KycExecutive;
import com.kyc.batch.executive.management.model.ProcessExecutiveRecord;
import com.kyc.core.persistence.entity.KycUser;
import com.kyc.core.persistence.repositories.KycUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.infrastructure.item.Chunk;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UpdatingUserItemWriterTest {

    @Mock
    private KycUserRepository kycUserRepository;

    @InjectMocks
    private UpdatingUserItemWriter updatingUserItemWriter;

    @Test
    public void write_updatingRecords_recordsWereSaved(){

        ProcessExecutiveRecord record = new ProcessExecutiveRecord();
        record.setKycExecutive(new KycExecutive());
        record.setKycUser(new KycUser());

        updatingUserItemWriter.write(new Chunk<>(Collections.singletonList(record)));
        verify(kycUserRepository,times(1)).save(any(KycUser.class));
    }
}
