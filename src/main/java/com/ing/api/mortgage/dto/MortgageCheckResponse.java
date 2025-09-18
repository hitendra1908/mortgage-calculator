package com.ing.api.mortgage.dto;

import java.math.BigDecimal;

public record MortgageCheckResponse(boolean feasible, BigDecimal monthlyCost) {
}
