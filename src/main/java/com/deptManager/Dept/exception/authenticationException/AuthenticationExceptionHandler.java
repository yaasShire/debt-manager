package com.deptManager.Dept.exception.authenticationException;

import com.deptManager.Dept.model.authenticationModel.AuthenticationExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseStatus
public class AuthenticationExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<AuthenticationExceptionResponse> departmentErrorHandler(AuthenticationException exeption, WebRequest request){
        AuthenticationExceptionResponse message = new AuthenticationExceptionResponse(HttpStatus.BAD_REQUEST, exeption.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}