package com.ing.api.mortgage.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record MortgageRateDto(int maturityPeriod, BigDecimal interestRate, Instant lastUpdate) {
}
