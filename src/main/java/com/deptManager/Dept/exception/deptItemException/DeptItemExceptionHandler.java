package com.deptManager.Dept.exception.deptItemException;

import com.deptManager.Dept.exception.bookException.BookException;
import com.deptManager.Dept.model.bookmodel.BookExceptionResponse;
import com.deptManager.Dept.model.deptItemModel.DeptItemExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseStatus
public class DeptItemExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(DeptItemException.class)
    public ResponseEntity<DeptItemExceptionResponse> departmentErrorHandler(DeptItemException exeption, WebRequest request){
        HttpStatus httpStatus = exeption.getHttpStatus();
        DeptItemExceptionResponse message = new DeptItemExceptionResponse(httpStatus, exeption.getMessage());
        return ResponseEntity.status(httpStatus).body(message);
    }
}