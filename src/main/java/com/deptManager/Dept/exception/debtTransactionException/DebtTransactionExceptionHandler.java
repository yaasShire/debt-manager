package com.deptManager.Dept.exception.debtTransactionException;

import com.deptManager.Dept.exception.pageException.PageException;
import com.deptManager.Dept.model.deptTransactionModel.DebtTransactionExceptionResponse;
import com.deptManager.Dept.model.pageModel.PageExceptionResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@ResponseStatus
public class DebtTransactionExceptionHandler {
    @ExceptionHandler(DebtTransactionException.class)
    public ResponseEntity<DebtTransactionExceptionResponse> departmentErrorHandler(DebtTransactionException exeption, WebRequest request){
        HttpStatus httpStatus = exeption.getHttpStatus();
        DebtTransactionExceptionResponse message = new DebtTransactionExceptionResponse(httpStatus, exeption.getMessage());
        return ResponseEntity.status(httpStatus).body(message);
    }
}
