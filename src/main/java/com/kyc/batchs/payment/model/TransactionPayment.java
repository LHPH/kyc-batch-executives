package com.kyc.batchs.payment.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class TransactionPayment {

    private Integer idTransaction;
    private String source;
    private String destination;
    private Integer idStatus;
    private Integer idBank;
    private Integer folioPayment;
    private Date dateStart;
    private Date dateFinish;
    private String comment;

}
