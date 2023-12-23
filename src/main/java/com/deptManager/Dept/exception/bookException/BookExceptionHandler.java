package com.deptManager.Dept.exception.bookException;

import com.deptManager.Dept.model.bookmodel.BookExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseStatus
public class BookExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BookException.class)
    public ResponseEntity<BookExceptionResponse> departmentErrorHandler(BookException exeption, WebRequest request){
        HttpStatus httpStatus = exeption.getHttpStatus();
        BookExceptionResponse message = new BookExceptionResponse(httpStatus, exeption.getMessage());
        return ResponseEntity.status(httpStatus).body(message);
    }
}