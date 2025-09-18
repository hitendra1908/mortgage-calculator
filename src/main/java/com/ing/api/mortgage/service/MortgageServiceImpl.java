package com.ing.api.mortgage.service;

import com.ing.api.mortgage.model.MortgageRate;
import com.ing.api.mortgage.dto.MortgageCheckRequest;
import com.ing.api.mortgage.dto.MortgageCheckResponse;
import com.ing.api.mortgage.dto.MortgageRateDto;
import com.ing.api.mortgage.exception.InvalidMaturityPeriodException;
import com.ing.api.mortgage.exception.mortgage.MortgageExceedsHomeValueException;
import com.ing.api.mortgage.exception.mortgage.MortgageExceedsIncomeLimitException;
import com.ing.api.mortgage.repository.MortgageRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MortgageServiceImpl implements MortgageService {

    private final MortgageRateRepository mortgageRateRepository;

    @Override
    public List<MortgageRateDto> getAllMortgageRates() {
        return mortgageRateRepository.findAll()
                .stream()
                .map(this::mapToMortgageRateDto)
                .toList();
    }

    private MortgageRateDto mapToMortgageRateDto(final MortgageRate rate) {
        return MortgageRateDto.builder()
                .interestRate(rate.getInterestRate())
                .maturityPeriod(rate.getMaturityPeriod())
                .lastUpdate(rate.getLastUpdate())
                .build();
    }

    @Override
    public MortgageCheckResponse checkMortgage(final MortgageCheckRequest request) {

        if (request.getLoanValue().compareTo(request.getIncome().multiply(new BigDecimal("4"))) > 0) {
            throw new MortgageExceedsIncomeLimitException(request.getLoanValue(), request.getIncome());
        }

        if (request.getLoanValue().compareTo(request.getHomeValue()) > 0) {
            throw new MortgageExceedsHomeValueException(request.getLoanValue(), request.getHomeValue());
        }

        MortgageRate rate = mortgageRateRepository.findById(request.getMaturityPeriod())
                .orElseThrow(() -> new InvalidMaturityPeriodException(request.getMaturityPeriod()));

        BigDecimal monthlyCost = calculateMonthlyPayment( request.getLoanValue(),
                rate.getInterestRate(),
                request.getMaturityPeriod());

        return new MortgageCheckResponse(true, monthlyCost);
    }


    /**
     * Calculates the monthly mortgage payment.
     *
     * @param principal           The loan amount (P)
     * @param mortgageRate  Annual interest rate as BigDecimal (e.g., 4.5 for 4.5%)
     * @param years               Loan term in years
     * @return Monthly payment as BigDecimal
     */
    private BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal mortgageRate, int years) {
        if (mortgageRate.compareTo(BigDecimal.ZERO) == 0) {
            // No interest case
            return principal.divide(BigDecimal.valueOf(years * 12L), 2, RoundingMode.HALF_UP);
        }

        BigDecimal monthlyRate = mortgageRate.divide(BigDecimal.valueOf(100 * 12), 10, RoundingMode.HALF_UP);
        int totalPayments = years * 12;

        // (1 + r)^n
        BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate);
        BigDecimal factor = onePlusRate.pow(totalPayments);

        // M = P * [r * (1 + r)^n] / [(1 + r)^n - 1]
        BigDecimal numerator = monthlyRate.multiply(factor);
        BigDecimal denominator = factor.subtract(BigDecimal.ONE);

        return principal.multiply(numerator).divide(denominator, 2, RoundingMode.HALF_UP);
    }
}
