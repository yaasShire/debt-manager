package com.deptManager.Dept.exception.s3Exception;

import com.deptManager.Dept.exception.pageException.PageException;
import com.deptManager.Dept.model.pageModel.PageExceptionResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@ResponseStatus
public class S3ExceptionHandler {
    @ExceptionHandler(PageException.class)
    public ResponseEntity<PageExceptionResponseModel> departmentErrorHandler(PageException exeption, WebRequest request){
        HttpStatus httpStatus = exeption.getHttpStatus();
        PageExceptionResponseModel message = new PageExceptionResponseModel(httpStatus, exeption.getMessage());
        return ResponseEntity.status(httpStatus).body(message);
    }
}