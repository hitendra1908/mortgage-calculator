package com.ing.api.mortgage.exception;

public class InvalidMaturityPeriodException extends RuntimeException {
    public InvalidMaturityPeriodException(int maturityYear) {
        super(String.format("Maturity period %s is invalid", maturityYear));
    }
}
