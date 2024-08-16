package com.kyc.batch.executive.management.writer;

import com.kyc.batch.executive.management.entity.KycExecutive;
import com.kyc.batch.executive.management.model.ProcessExecutiveRecord;
import com.kyc.batch.executive.management.repository.KycExecutiveRepository;
import com.kyc.core.persistence.entity.KycUser;
import com.kyc.core.persistence.repositories.KycUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@AllArgsConstructor
public class RegistrationUserItemWriter implements ItemWriter<ProcessExecutiveRecord> {

    private KycExecutiveRepository kycExecutiveRepository;
    private KycUserRepository kycUserRepository;

    @Override
    public void write(Chunk<? extends ProcessExecutiveRecord> chunk) {

        List<? extends  ProcessExecutiveRecord> list = chunk.getItems();
        for(ProcessExecutiveRecord wrapper: list){
            KycUser newUser = kycUserRepository.save(wrapper.getKycUser());
            KycExecutive executive = wrapper.getKycExecutive();
            executive.setIdUser(newUser.getId());
            kycExecutiveRepository.saveAndFlush(executive);
        }
    }
}
