package com.deptManager.Dept.exception.s3Exception;

import org.springframework.http.HttpStatus;

public class S3Exception extends Exception{
    private final HttpStatus httpStatus;

    public S3Exception(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public S3Exception(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public S3Exception(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public S3Exception(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
