package com.ing.api.mortgage.exception.mortgage;

import java.math.BigDecimal;

public non-sealed class MortgageExceedsIncomeLimitException extends MortgageException {
    public MortgageExceedsIncomeLimitException(BigDecimal loanValue, BigDecimal income) {
        super(String.format("Mortgage value of %s exceeds the allowed limit of 4x the income: %s", loanValue, income));
    }
}

