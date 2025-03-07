package com.ptit.asset.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ApiError {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private List<ApiSubError> subErrors;
    private Map<String,String> validationError;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    // TODO : Constructor 1
    ApiError(HttpStatus status) {
        this();
        this.status = status;
    }
    // TODO : Constructor 2
    ApiError(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }
    // TODO : Constructor 3
    ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }
    // TODO : Constructor 4
    ApiError(HttpStatus status, String message, Map<String,String> validationError, Throwable ex){
        this();
        this.status = status;
        this.message = message;
        this.validationError = validationError;
        this.debugMessage = ex.getLocalizedMessage();
    }

    // getter setter ================================================


    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public List<ApiSubError> getSubErrors() {
        return subErrors;
    }

    public void setSubErrors(List<ApiSubError> subErrors) {
        this.subErrors = subErrors;
    }

    public Map<String, String> getValidationError() {
        return validationError;
    }

    public void setValidationError(Map<String, String> validationError) {
        this.validationError = validationError;
    }
}
