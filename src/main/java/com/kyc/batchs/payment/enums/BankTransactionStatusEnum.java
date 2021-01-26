package com.kyc.batchs.payment.enums;

import lombok.Getter;

@Getter
public enum BankTransactionStatusEnum {

    SUCCESS("SU",1),
    APPROVED("AP",2),
    PENDING("PE",3),
    SEND("SE",4),
    DECLINED("DE",5),
    FAILED("FA",6);

    private String status;
    private Integer id;

    private BankTransactionStatusEnum(String status,Integer id){
        this.status = status;
        this.id = id;
    }

    public static BankTransactionStatusEnum getInstance(String code){

        for(BankTransactionStatusEnum value : BankTransactionStatusEnum.values()){
            if(value.getStatus().equals(code)){
                return value;
            }
        }
        return null;
    }
}
