package com.kyc.batch.executive.management.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum BatchExecutiveProcessEnum {

    OTHER(-1),
    REGISTRATION(0),
    MODIFICATION(1),
    DISABLING(2),
    ENABLING(3),
    PROCESSED(4);

    private int operation;

    public static BatchExecutiveProcessEnum getInstance(Integer value){

        BatchExecutiveProcessEnum result = OTHER;
        Integer operation = ObjectUtils.defaultIfNull(value,-1);

        for(BatchExecutiveProcessEnum batchExecutiveProcessEnum : BatchExecutiveProcessEnum.values()){

            if(batchExecutiveProcessEnum.getOperation() == operation){
                result = batchExecutiveProcessEnum;
                break;
            }
        }
        return result;
    }
}
