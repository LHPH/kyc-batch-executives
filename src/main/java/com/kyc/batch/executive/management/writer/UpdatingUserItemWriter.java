package com.kyc.batch.executive.management.writer;

import com.kyc.batch.executive.management.model.ProcessExecutiveRecord;
import com.kyc.batch.executive.management.repository.KycExecutiveRepository;
import com.kyc.batch.executive.management.repository.KycUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@AllArgsConstructor
public class UpdatingUserItemWriter implements ItemWriter<ProcessExecutiveRecord> {

    private KycUserRepository kycUserRepository;

    @Override
    public void write(List<? extends ProcessExecutiveRecord> list) {

        for(ProcessExecutiveRecord wrapper : list){

            kycUserRepository.save(wrapper.getKycUser());
        }
    }
}
