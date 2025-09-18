package com.ing.api.mortgage.exception.mortgage;

import java.math.BigDecimal;

public non-sealed class MortgageExceedsHomeValueException extends MortgageException {
    public MortgageExceedsHomeValueException(BigDecimal loanValue, BigDecimal homeValue) {
        super(String.format("Mortgage value of %s exceeds the total home value (%s)", loanValue, homeValue));
    }
}

