package com.kyc.batchs.executive.management.writer;

import com.kyc.batchs.executive.management.entity.KycExecutive;
import com.kyc.batchs.executive.management.entity.KycUser;
import com.kyc.batchs.executive.management.model.ProcessExecutiveRecord;
import com.kyc.batchs.executive.management.repository.KycExecutiveRepository;
import com.kyc.batchs.executive.management.repository.KycUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@AllArgsConstructor
public class RegistrationUserItemWriter implements ItemWriter<ProcessExecutiveRecord> {

    private KycExecutiveRepository kycExecutiveRepository;
    private KycUserRepository kycUserRepository;

    @Override
    public void write(List<? extends ProcessExecutiveRecord> list) {

        for(ProcessExecutiveRecord wrapper: list){
            KycUser newUser = kycUserRepository.save(wrapper.getKycUser());
            KycExecutive executive = wrapper.getKycExecutive();
            executive.setIdUser(newUser.getId());
            kycExecutiveRepository.saveAndFlush(executive);
        }
    }
}
