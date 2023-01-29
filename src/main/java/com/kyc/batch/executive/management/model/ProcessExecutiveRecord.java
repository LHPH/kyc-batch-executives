package com.kyc.batch.executive.management.model;

import com.kyc.batch.executive.management.entity.KycExecutive;
import com.kyc.batch.executive.management.entity.KycUser;
import com.kyc.batch.executive.management.enums.BatchExecutiveProcessEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProcessExecutiveRecord {

    private BatchExecutiveProcessEnum operation;
    private KycExecutive kycExecutive;
    private KycUser kycUser;
}
