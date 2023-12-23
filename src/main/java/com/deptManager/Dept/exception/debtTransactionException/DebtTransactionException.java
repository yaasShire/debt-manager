package com.deptManager.Dept.exception.debtTransactionException;

import org.springframework.http.HttpStatus;

public class DebtTransactionException extends Exception{
    private final HttpStatus httpStatus;

    public DebtTransactionException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public DebtTransactionException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public DebtTransactionException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public DebtTransactionException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
