package com.kyc.batchs.executive.management.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ExecutiveRawData {

    private Integer operation;
    private Long executiveNumber;
    private String firstName;
    private String secondName;
    private String lastName;
    private String secondLastName;
    private String rfc;
    private String phone;
    private String extension;
    private String email;
    private Integer idBranch;
    private Integer active;

}
