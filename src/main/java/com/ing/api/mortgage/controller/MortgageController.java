package com.ing.api.mortgage.controller;

import com.ing.api.mortgage.dto.MortgageCheckRequest;
import com.ing.api.mortgage.dto.MortgageCheckResponse;
import com.ing.api.mortgage.dto.MortgageRateDto;
import com.ing.api.mortgage.service.MortgageServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Mortgage API", description = "Endpoints for mortgage interest rates and feasibility checks")
public class MortgageController {

    private final MortgageServiceImpl mortgageServiceImpl;

    @Operation(summary = "Get current interest rates", description = "Returns a list of available mortgage interest rates")
    @GetMapping("/interest-rates")
    public List<MortgageRateDto> getRates() {
        return mortgageServiceImpl.getAllMortgageRates();
    }

    @Operation(summary = "Check mortgage feasibility", description = "Calculates if a mortgage is feasible and returns monthly cost")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mortgage check result"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/mortgage-check")
    public ResponseEntity<MortgageCheckResponse> checkMortgage(
            @Valid @RequestBody MortgageCheckRequest request) {
        return ResponseEntity.ok(mortgageServiceImpl.checkMortgage(request));
    }
}

