package com.ing.api.mortgage.exception.mortgage;

public sealed class MortgageException extends RuntimeException permits MortgageExceedsIncomeLimitException, MortgageExceedsHomeValueException {
    public MortgageException(String message) {
        super(message);
    }
}
