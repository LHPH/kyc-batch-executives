package com.kyc.batch.executive.management.writer;

import com.kyc.batch.executive.management.model.ProcessExecutiveRecord;
import com.kyc.core.persistence.repositories.KycUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@AllArgsConstructor
public class UpdatingUserItemWriter implements ItemWriter<ProcessExecutiveRecord> {

    private KycUserRepository kycUserRepository;

    @Override
    public void write(Chunk<? extends ProcessExecutiveRecord> chunk) {

        List<? extends  ProcessExecutiveRecord> list = chunk.getItems();
        for(ProcessExecutiveRecord wrapper : list){

            kycUserRepository.save(wrapper.getKycUser());
        }
    }
}
