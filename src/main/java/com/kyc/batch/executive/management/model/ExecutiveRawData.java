package com.kyc.batch.executive.management.model;

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


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append(operation);
        sb.append(",").append(executiveNumber);
        sb.append(",").append(firstName);
        sb.append(",").append(secondName);
        sb.append(",").append(lastName);
        sb.append(",").append(secondLastName);
        sb.append(",").append(rfc);
        sb.append(",").append(phone);
        sb.append(",").append(extension);
        sb.append(",").append(email);
        sb.append(",").append(idBranch);
        return sb.toString();
    }
}
