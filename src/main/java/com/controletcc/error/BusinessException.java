package com.controletcc.error;

public class BusinessException extends Exception {

    public BusinessException(String msg) {
        super(msg);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
