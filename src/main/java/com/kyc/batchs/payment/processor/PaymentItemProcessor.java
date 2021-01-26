package com.kyc.batchs.payment.processor;

import com.kyc.batchs.payment.enums.BankTransactionStatusEnum;
import com.kyc.batchs.payment.model.BankResponse;
import com.kyc.batchs.payment.model.TransactionPayment;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

import static com.kyc.batchs.util.DateUtil.parseStringToDate;

public class PaymentItemProcessor implements ItemProcessor<BankResponse, TransactionPayment> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String bank;

    public PaymentItemProcessor(String bank){
        this.bank = bank;
    }

    @Override
    public TransactionPayment process(BankResponse bankResponse) throws Exception {


        TransactionPayment transactionPayment = new TransactionPayment();
        Optional<BankTransactionStatusEnum> opStatus = Optional
                .ofNullable(BankTransactionStatusEnum.getInstance(bankResponse.getCode()));

        transactionPayment.setIdTransaction(Integer.valueOf(bankResponse.getId()));
        transactionPayment.setDateStart(parseStringToDate(bankResponse.getDateStart(),"yyyyMMdd"));
        transactionPayment.setDateFinish(parseStringToDate(bankResponse.getDateFinish(),"yyyyMMdd"));
        transactionPayment.setDestination(this.bank);
        transactionPayment.setSource(this.bank);
        transactionPayment.setComment(bankResponse.getComment());
        transactionPayment.setIdStatus(opStatus.orElse(BankTransactionStatusEnum.FAILED).getId());

        transactionPayment.setFolioPayment(null);
        transactionPayment.setIdBank(null);

        return transactionPayment;
    }
}
