package com.ptit.asset.exception;

public class InvalidProductResourcesException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidProductResourcesException(String cause){
        super(cause);
    }
}
