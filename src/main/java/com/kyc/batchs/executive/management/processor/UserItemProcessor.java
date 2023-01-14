package com.kyc.batchs.executive.management.processor;

import com.kyc.batchs.executive.management.entity.KycExecutive;
import com.kyc.batchs.executive.management.entity.KycUser;
import com.kyc.batchs.executive.management.enums.ExecutiveMngEnum;
import com.kyc.batchs.executive.management.model.ProcessExecutiveRecord;
import com.kyc.batchs.executive.management.repository.KycUserRepository;
import com.kyc.core.services.PasswordEncoderService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;

import java.util.Date;

@AllArgsConstructor
public class UserItemProcessor implements ItemProcessor<KycExecutive, ProcessExecutiveRecord> {

    private KycUserRepository kycUserRepository;
    private PasswordEncoderService passwordEncoderService;

    @Override
    public ProcessExecutiveRecord process(KycExecutive kycExecutive) {

        ProcessExecutiveRecord wrapper = new ProcessExecutiveRecord();

        KycUser kycUser;

        ExecutiveMngEnum operation = ExecutiveMngEnum.getInstance(kycExecutive.getBatchOperationControl());

        switch(operation){
            case REGISTRATION:
                kycUser = new KycUser();
                kycUser.setUsername("kycEx"+ StringUtils.leftPad(kycExecutive.getId().toString(),5,"0"));
                kycUser.setSecret(passwordEncoderService.encode("P4sW$Ord"));
                kycUser.setActive(true);
                kycUser.setLocked(false);
                kycUser.setUserType(2);
                kycUser.setDateCreation(new Date());

                wrapper.setOperation(ExecutiveMngEnum.REGISTRATION);
                break;
            case DISABLING:
                kycUser = kycUserRepository.getReferenceById(kycExecutive.getIdUser());

                wrapper.setOperation(ExecutiveMngEnum.DISABLING);
                kycUser.setActive(false);
                kycUser.setDateUpdated(new Date());

                break;
            case ENABLING:

                kycUser = kycUserRepository.getReferenceById(kycExecutive.getIdUser());

                wrapper.setOperation(ExecutiveMngEnum.ENABLING);
                kycUser.setActive(true);
                kycUser.setDateUpdated(new Date());
                break;
            default:
                return null;
        }

        kycExecutive.setBatchOperationControl(ExecutiveMngEnum.PROCESSED.getOperation());
        wrapper.setKycExecutive(kycExecutive);
        wrapper.setKycUser(kycUser);
        return wrapper;
    }
}
