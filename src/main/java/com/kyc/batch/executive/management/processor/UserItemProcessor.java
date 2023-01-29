package com.kyc.batch.executive.management.processor;

import com.kyc.batch.executive.management.entity.KycExecutive;
import com.kyc.batch.executive.management.entity.KycUser;
import com.kyc.batch.executive.management.enums.BatchExecutiveProcessEnum;
import com.kyc.batch.executive.management.model.ProcessExecutiveRecord;
import com.kyc.batch.executive.management.repository.KycUserRepository;
import com.kyc.core.services.PasswordEncoderService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.Date;

@AllArgsConstructor
public class UserItemProcessor implements ItemProcessor<KycExecutive, ProcessExecutiveRecord> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserItemProcessor.class);

    private KycUserRepository kycUserRepository;
    private PasswordEncoderService passwordEncoderService;

    @Override
    public ProcessExecutiveRecord process(KycExecutive kycExecutive) {

        ProcessExecutiveRecord wrapper = new ProcessExecutiveRecord();

        KycUser kycUser;

        BatchExecutiveProcessEnum operation = BatchExecutiveProcessEnum.getInstance(kycExecutive.getBatchOperationControl());
        LOGGER.info("Procesando Usuario {}",kycExecutive.getId());
        switch(operation){
            case REGISTRATION:
                kycUser = new KycUser();
                kycUser.setUsername("kycEx"+ StringUtils.leftPad(kycExecutive.getId().toString(),5,"0"));
                kycUser.setSecret(passwordEncoderService.encode("P4sW$Ord"));
                kycUser.setActive(true);
                kycUser.setLocked(false);
                kycUser.setUserType(2);
                kycUser.setDateCreation(new Date());

                wrapper.setOperation(BatchExecutiveProcessEnum.REGISTRATION);
                break;
            case DISABLING:
                kycUser = kycUserRepository.getReferenceById(kycExecutive.getIdUser());

                wrapper.setOperation(BatchExecutiveProcessEnum.DISABLING);
                kycUser.setActive(false);
                kycUser.setDateUpdated(new Date());

                break;
            case ENABLING:

                kycUser = kycUserRepository.getReferenceById(kycExecutive.getIdUser());

                wrapper.setOperation(BatchExecutiveProcessEnum.ENABLING);
                kycUser.setActive(true);
                kycUser.setDateUpdated(new Date());
                break;
            default:
                return null;
        }

        wrapper.setKycExecutive(kycExecutive);
        wrapper.setKycUser(kycUser);
        return wrapper;
    }
}
