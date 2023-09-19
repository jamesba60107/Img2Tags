package com.Img2Tags.exception;

public class ApiRequestException extends RuntimeException {

    private final int errorCode;
    public ApiRequestException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
