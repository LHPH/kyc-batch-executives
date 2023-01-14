package com.kyc.batchs.commons.exception;

public class KycBatchException extends RuntimeException{

    public KycBatchException() {
    }

    public KycBatchException(String message) {
        super(message);
    }

    public KycBatchException(Throwable cause) {
        super(cause);
    }
}
