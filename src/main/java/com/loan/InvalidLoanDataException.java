package com.loan;

public class InvalidLoanDataException extends RuntimeException {

    public InvalidLoanDataException(String message) {
        super(message);
    }
}
