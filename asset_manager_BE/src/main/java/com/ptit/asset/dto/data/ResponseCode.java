package com.ptit.asset.dto.data;

public enum ResponseCode {
    SUCCESS("Success", 200),
    FAILURE("Failure", 400);

    public String responseMessage;
    public Integer code;
    ResponseCode(String responseMessage, Integer code){
        this.responseMessage = responseMessage;
        this.code = code;
    }
}
