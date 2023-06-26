package com.mindex.challenge.service.exception;

public class CompensationNotFoundException extends RuntimeException {
    public CompensationNotFoundException(String employeeId) {
        super(String.format("Attempted to lookup non-existing compensation for employee with id [%s]", employeeId));
    }
}
