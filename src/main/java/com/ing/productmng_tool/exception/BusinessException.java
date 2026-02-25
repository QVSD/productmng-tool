package com.ing.productmng_tool.exception;

public abstract class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}