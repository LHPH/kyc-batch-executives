package com.kyc.batchs.payment.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Setter
@Getter
@Table(value = "TRANSACTIONS")
public class TransactionEntity {

    @Id
    @Column(value = "ID")
    private Integer id;

    @Column(value = "SOURCE")
    private String source;

    @Column(value = "DESTINATION")
    private String destination;

    @Column(value="ID_STATUS")
    private Integer idStatus;

    @Column(value = "ID_BANK")
    private Integer idBank;

    @Column(value ="ID_FOLIO_PAYMENT")
    private Integer idPayment;

    @Column(value = "DATE_START")
    private Timestamp dateStart;

    @Column(value = "DATE_FINISH")
    private Timestamp dateFinish;

}
