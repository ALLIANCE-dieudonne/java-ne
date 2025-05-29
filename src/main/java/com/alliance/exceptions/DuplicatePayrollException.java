package com.alliance.exceptions;

public class DuplicatePayrollException extends RuntimeException {
    public DuplicatePayrollException(String message) {
        super(message);
    }
}
