package com.ing.api.mortgage.service;

import com.ing.api.mortgage.dto.MortgageCheckRequest;
import com.ing.api.mortgage.dto.MortgageCheckResponse;
import com.ing.api.mortgage.dto.MortgageRateDto;

import java.util.List;

public interface MortgageService {
    List<MortgageRateDto> getAllMortgageRates();
    MortgageCheckResponse checkMortgage(final MortgageCheckRequest request);
}
