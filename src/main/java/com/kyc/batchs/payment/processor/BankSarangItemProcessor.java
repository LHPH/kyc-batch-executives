package com.kyc.batchs.payment.processor;

import com.kyc.batchs.payment.entity.BankEntity;
import com.kyc.batchs.payment.enums.BankTransactionStatusEnum;
import com.kyc.batchs.payment.model.BankResponse;
import com.kyc.batchs.payment.model.TransactionPayment;
import com.kyc.batchs.payment.repository.BankRepository;
import com.kyc.batchs.payment.repository.NativeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

import static com.kyc.batchs.util.DateUtil.parseStringToDate;

public class PaymentItemProcessor implements ItemProcessor<BankResponse, TransactionPayment> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private NativeRepository nativeRepository;

    @Autowired
    private BankRepository bankRepository;

    private String bank;

    public PaymentItemProcessor(String bank){
        this.bank = bank;
    }

    @Override
    public TransactionPayment process(BankResponse bankResponse) throws Exception {


        TransactionPayment transactionPayment = new TransactionPayment();



        Optional<BankTransactionStatusEnum> opStatus = Optional
                .ofNullable(BankTransactionStatusEnum.getInstance(bankResponse.getCode()));

        transactionPayment.setDateStart(parseStringToDate(bankResponse.getDateStart(),"yyyyMMdd"));
        transactionPayment.setDateFinish(parseStringToDate(bankResponse.getDateFinish(),"yyyyMMdd"));
        transactionPayment.setDestination(this.bank);
        transactionPayment.setSource(this.bank);
        transactionPayment.setComment(bankResponse.getComment());
        transactionPayment.setIdStatus(opStatus.orElse(BankTransactionStatusEnum.FAILED).getId());

        String reference = bankResponse.getReference();

        BankEntity bank = bankRepository.getBankByCve(this.bank);
        Long folio = nativeRepository.getFolioByReference(reference);

        transactionPayment.setFolioPayment(folio);
        transactionPayment.setIdBank(bank.getId());

        return transactionPayment;
    }
}
