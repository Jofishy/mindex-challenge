package com.mindex.challenge.service.exception;

public class DuplicateCompensationException extends RuntimeException {
    public DuplicateCompensationException(String employeeId) {
        super(String.format("Attempted to create a duplicate compensation for employee [%s]", employeeId));
    }
}
