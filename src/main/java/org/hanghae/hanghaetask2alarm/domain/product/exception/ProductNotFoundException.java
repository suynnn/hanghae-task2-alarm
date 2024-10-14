package org.hanghae.hanghaetask2alarm.domain.product.exception;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(String message) {
        super(message);
    }
}
