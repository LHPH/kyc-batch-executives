package com.kyc.batchs.executive.management.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExecutiveMngEnum {

    OTHER(-1),
    REGISTRATION(0),
    MODIFICATION(1),
    DISABLING(2),
    ENABLING(3),
    PROCESSED(4);

    private int operation;

    public static ExecutiveMngEnum getInstance(Integer value){

        ExecutiveMngEnum result = OTHER;
        Integer operation = ObjectUtils.defaultIfNull(value,-1);

        for(ExecutiveMngEnum executiveMngEnum : ExecutiveMngEnum.values()){

            if(executiveMngEnum.getOperation() == operation){
                result = executiveMngEnum;
                break;
            }
        }
        return result;
    }
}
