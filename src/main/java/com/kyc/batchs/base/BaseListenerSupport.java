package com.kyc.batchs.base;

import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.listener.StepListenerSupport;

public abstract class BaseListenerSupport<I,O> extends StepListenerSupport<I,O> {

    public abstract ItemProcessListener<I,O> getListenerSupport();

}
