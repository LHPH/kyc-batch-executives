package com.kyc.batchs.payment.mappers;

import com.kyc.batchs.base.BaseMapper;
import com.kyc.batchs.payment.entity.TransactionEntity;
import com.kyc.batchs.payment.model.TransactionPayment;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class TransactionMapper extends BaseMapper<TransactionPayment, TransactionEntity> {


    @Override
    public TransactionEntity toEntity(TransactionPayment model) {

        TransactionEntity transactionEntity = new TransactionEntity();

        transactionEntity.setId(model.getIdTransaction());
        transactionEntity.setSource(model.getSource());
        transactionEntity.setDestination(model.getDestination());
        transactionEntity.setIdBank(model.getIdBank());
        transactionEntity.setIdStatus(model.getIdStatus());
        transactionEntity.setIdPayment(model.getFolioPayment());
        transactionEntity.setDateStart(new Timestamp(model.getDateStart().getTime()));
        transactionEntity.setDateFinish(new Timestamp(model.getDateFinish().getTime()));

        return transactionEntity;
    }

    @Override
    public TransactionPayment toModel(TransactionEntity entity) {

        TransactionPayment transactionPayment = new TransactionPayment();

        transactionPayment.setIdTransaction(entity.getId());
        transactionPayment.setSource(entity.getSource());
        transactionPayment.setDestination(entity.getDestination());
        transactionPayment.setIdBank(entity.getIdBank());
        transactionPayment.setIdStatus(entity.getIdStatus());
        transactionPayment.setFolioPayment(entity.getIdPayment());
        transactionPayment.setDateStart(entity.getDateStart());
        transactionPayment.setDateFinish(entity.getDateFinish());

        return transactionPayment;
    }
}
