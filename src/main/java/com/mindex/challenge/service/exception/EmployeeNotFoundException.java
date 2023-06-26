package com.mindex.challenge.service.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(final String id){
        super(String.format("Attempted to lookup missing employeeId [%s]", id));
    }
}
