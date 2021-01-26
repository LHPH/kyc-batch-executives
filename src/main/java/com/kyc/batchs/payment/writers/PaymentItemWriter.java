package com.kyc.batchs.payment.writers;

import com.kyc.batchs.payment.entity.TransactionEntity;
import com.kyc.batchs.payment.mappers.TransactionMapper;
import com.kyc.batchs.payment.model.TransactionPayment;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentItemWriter implements ItemWriter<TransactionPayment> {

    private final static Logger LOGGER = LoggerFactory.getLogger(PaymentItemWriter.class);

    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void write(List<? extends TransactionPayment> list) throws Exception {

        if(!list.isEmpty()){

            List<TransactionEntity> entities = list.stream()
                    .map(e ->transactionMapper.toEntity(e)).collect(Collectors.toList());

            LOGGER.info("** {}",entities);

            //namedParameterJdbcTemplate.batchUpdate()

        }

    }
}
