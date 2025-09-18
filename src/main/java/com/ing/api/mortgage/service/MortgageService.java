package com.ing.api.mortgage.service;

import com.ing.api.mortgage.dto.MortgageCheckRequest;
import com.ing.api.mortgage.dto.MortgageCheckResponse;
import com.ing.api.mortgage.dto.MortgageRateDto;

import java.util.List;

public interface MortgageService {
    public List<MortgageRateDto> getAllMortgageRates();
    public MortgageCheckResponse checkMortgage(MortgageCheckRequest request);
}
