package com.ing.productmng_tool.exception;

public class ProductNotFoundException extends BusinessException {

    public ProductNotFoundException(String message) {
        super(message);
    }
}