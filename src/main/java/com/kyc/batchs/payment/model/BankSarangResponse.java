package com.kyc.batchs.payment.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BankResponse {

    private String dateStart;
    private String dateFinish;
    private String code;
    private String reference;
    private String comment;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("code='").append(code).append('\'');
        sb.append(", reference='").append(reference).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
