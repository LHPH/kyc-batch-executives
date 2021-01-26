package com.kyc.batchs.base;

public abstract class BaseMapper<M,E> {

    public abstract E toEntity(M model);

    public abstract M toModel(E entity);

}
