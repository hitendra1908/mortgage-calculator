package com.ing.api.mortgage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "mortgage_rate")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MortgageRate {

    @Id
    private int maturityPeriod;

    private BigDecimal interestRate;

    private Instant lastUpdate;
}
