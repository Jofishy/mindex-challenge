package com.mindex.challenge.controller;

import com.mindex.challenge.data.ErrorResponse;
import com.mindex.challenge.service.exception.CompensationNotFoundException;
import com.mindex.challenge.service.exception.DuplicateCompensationException;
import com.mindex.challenge.service.exception.EmployeeNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(value = {EmployeeNotFoundException.class, CompensationNotFoundException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleEntityNotFound(RuntimeException ex){
        ErrorResponse response = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {DuplicateCompensationException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleDuplicateCompensation(RuntimeException ex){
        ErrorResponse response = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseBody
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex){
        ErrorResponse response = new ErrorResponse("Validation exception");
        ex.getBindingResult().getAllErrors().forEach(e->response.addDetail(e.getDefaultMessage()));
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
