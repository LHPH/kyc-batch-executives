package com.kyc.batchs.executive.management.model;

import com.kyc.batchs.executive.management.entity.KycExecutive;
import com.kyc.batchs.executive.management.entity.KycUser;
import com.kyc.batchs.executive.management.enums.ExecutiveMngEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProcessExecutiveRecord {

    private ExecutiveMngEnum operation;
    private KycExecutive kycExecutive;
    private KycUser kycUser;
}
