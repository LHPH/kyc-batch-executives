package com.kyc.batchs.executive.management.processor;

import com.kyc.batchs.executive.management.entity.KycExecutive;
import com.kyc.batchs.executive.management.enums.ExecutiveMngEnum;
import com.kyc.batchs.executive.management.mappers.ExecutiveMapper;
import com.kyc.batchs.executive.management.model.ExecutiveRawData;
import com.kyc.batchs.executive.management.repository.KycExecutiveRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.Date;

@AllArgsConstructor
public class ExecutiveItemProcessor implements ItemProcessor<ExecutiveRawData, KycExecutive> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutiveItemProcessor.class);

    private ExecutiveMapper executiveMapper;
    private KycExecutiveRepository kycExecutiveRepository;

    @Override
    public KycExecutive process(ExecutiveRawData executiveRawData) {

        LOGGER.info("Processor");

        KycExecutive kycExecutive;
        Long idExecutive = executiveRawData.getExecutiveNumber();
        switch (ExecutiveMngEnum.getInstance(executiveRawData.getOperation())) {

            case REGISTRATION:
                return executiveMapper.toNewKycExecutive(executiveRawData);

            case MODIFICATION:
                kycExecutive = kycExecutiveRepository.getReferenceById(idExecutive);
                executiveMapper.updateKycExecutive(executiveRawData,kycExecutive);
                return kycExecutive;

            case DISABLING:
                kycExecutive = kycExecutiveRepository.getReferenceById(idExecutive);
                kycExecutive.setStatus(false);
                kycExecutive.setUpdatedDate(new Date());
                kycExecutive.setBatchOperationControl(ExecutiveMngEnum.DISABLING.getOperation());
                return kycExecutive;

            case ENABLING:
                kycExecutive = kycExecutiveRepository.getReferenceById(idExecutive);
                kycExecutive.setStatus(true);
                kycExecutive.setUpdatedDate(new Date());
                kycExecutive.setBatchOperationControl(ExecutiveMngEnum.ENABLING.getOperation());
                return kycExecutive;

        }
        return null;
    }
}
