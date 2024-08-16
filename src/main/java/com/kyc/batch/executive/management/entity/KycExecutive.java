package com.kyc.batch.executive.management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "KYC_EXECUTIVE")
@Setter
@Getter
public class KycExecutive implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="FIRST_NAME")
    private String firstName;

    @Column(name ="SECOND_NAME")
    private String secondName;

    @Column(name ="LAST_NAME")
    private String lastName;

    @Column(name ="SECOND_LAST_NAME")
    private String secondLastName;

    @Column(name ="RFC")
    private String rfc;

    @Column(name ="PHONE")
    private String phone;

    @Column(name ="EXTENSION")
    private String extension;

    @Column(name ="EMAIL")
    private String email;

    @Column(name ="ID_BRANCH")
    private Integer idBranch;

    @Column(name ="STATUS")
    private Boolean status;

    @Column(name ="DATE_CREATION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name ="DATE_UPDATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Column(name = "ID_USER")
    private Long idUser;

    @Column(name = "BATCH_OPERATION_CONTROL")
    private Integer batchOperationControl;
}
