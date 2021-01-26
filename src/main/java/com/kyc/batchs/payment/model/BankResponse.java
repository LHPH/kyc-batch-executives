package com.kyc.batchs.payment.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BankResponse {

    private String id;
    private String dateStart;
    private String dateFinish;
    private String code;
    private String reference;
    private String comment;

}
