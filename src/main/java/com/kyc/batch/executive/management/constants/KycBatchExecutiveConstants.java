package com.kyc.batch.executive.management.constants;

import com.kyc.core.constants.BatchConstants;

public final class KycBatchExecutiveConstants {

    public static final String JOB_NAME = String.format(BatchConstants.KYC_BATCH_JOB_TEMPLATE,
            "EXECUTIVES","ADM-EXECUTIVES");
    public static final String ADM_EXECUTIVE_STEP = String.format(BatchConstants.KYC_BATCH_STEP_TEMPLATE,
            "EXECUTIVES","EXECUTIVE-DATA");
    public static final String ADM_USERS_STEP = String.format(BatchConstants.KYC_BATCH_STEP_TEMPLATE,
            "EXECUTIVES","MNG-USERS");
    public static final String CLEAN_FILE_TASK = String.format(BatchConstants.KYC_BATCH_TASKLET_TEMPLATE,
            "EXECUTIVES","CLEAN-FILE");
}
