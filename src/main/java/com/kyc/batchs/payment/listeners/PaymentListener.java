package com.kyc.batchs.payment.listeners;

import com.kyc.batchs.base.BaseListenerSupport;
import com.kyc.batchs.payment.model.BankResponse;
import com.kyc.batchs.payment.model.TransactionPayment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.listener.StepListenerSupport;

import java.lang.invoke.MethodHandles;
import java.util.List;

public  class PaymentListener extends BaseListenerSupport<BankResponse, TransactionPayment> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void afterRead(BankResponse item) {
        LOGGER.info("Se leyo el objeto {}",item);
    }

    @Override
    public void beforeRead() {
        LOGGER.info("Comenzando a leer registros");
    }

    @Override
    public void onReadError(Exception ex) {
        LOGGER.error("Ocurrio un error en la lectura ",ex);
    }

    @Override
    public void afterProcess(BankResponse item, TransactionPayment result) {
        LOGGER.info("Se ha procesado la respuesta {} con el resultado {}",item,result);
    }

    @Override
    public void beforeProcess(BankResponse item) {
        LOGGER.info("Registro a procesar {}",item);
    }

    @Override
    public void onProcessError(BankResponse item, Exception e) {
        LOGGER.error("Ocurrio un error procesando el registro {}",item);
        LOGGER.error(" ",e);
    }

    @Override
    public void afterWrite(List<? extends TransactionPayment> item) {
        LOGGER.info("**Los registros a escribirse {}",item);
    }

    @Override
    public void beforeWrite(List<? extends TransactionPayment> item) {
        LOGGER.info("++Los registros a escribirse {}",item);
    }

    @Override
    public void onWriteError(Exception ex, List<? extends TransactionPayment> item) {
        LOGGER.error("Ocurrio un error al escribir {}",item);
        LOGGER.error(" ",ex);
    }

    public ItemProcessListener<BankResponse, TransactionPayment> asItemProcessListener(){
        return this;
    }

    @Override
    public ItemProcessListener<BankResponse, TransactionPayment> getListenerSupport() {
        return this;
    }
}
