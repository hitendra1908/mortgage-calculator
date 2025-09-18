package com.ing.api.mortgage.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class MortgageCheckRequest {

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal income;

    @NotNull
    @Min(1)
    private Integer maturityPeriod;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal loanValue;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal homeValue;

    private String name; //optional
}
