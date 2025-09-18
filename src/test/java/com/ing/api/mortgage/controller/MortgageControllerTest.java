package com.ing.api.mortgage.controller;

import com.ing.api.mortgage.dto.MortgageCheckRequest;
import com.ing.api.mortgage.dto.MortgageCheckResponse;
import com.ing.api.mortgage.dto.MortgageRateDto;
import com.ing.api.mortgage.service.MortgageServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MortgageControllerTest {

    @Mock
    private MortgageServiceImpl mortgageService;

    @InjectMocks
    private MortgageController mortgageController;

    @Nested
    @DisplayName("get mortgage Rates Tests")
    class GetMortgageRatesTests {

        @Test
        @DisplayName("Should return list of mortgage rates")
        void shoutReturnRates() {
            List<MortgageRateDto> expectedRates = List.of(
                    new MortgageRateDto(10, BigDecimal.valueOf(3.5), Instant.now()),
                    new MortgageRateDto(20, BigDecimal.valueOf(2.8), Instant.now())
            );
            when(mortgageService.getAllMortgageRates()).thenReturn(expectedRates);

            List<MortgageRateDto> actualRates = mortgageController.getRates();

            assertNotNull(actualRates);
            assertThat(actualRates.size()).isEqualTo(2);
            assertThat(actualRates.getFirst().maturityPeriod()).isEqualTo(10);
            verify(mortgageService, times(1)).getAllMortgageRates();
        }

        @Test
        @DisplayName("Should return empty list when no rates available")
        void shouldReturnEmptyList() {
            when(mortgageService.getAllMortgageRates()).thenReturn(Collections.emptyList());

            List<MortgageRateDto> actualRates = mortgageController.getRates();

            assertNotNull(actualRates);
            assertTrue(actualRates.isEmpty());
            verify(mortgageService, times(1)).getAllMortgageRates();
        }
    }

    @Nested
    @DisplayName("checkMortgage Tests")
    class CheckMortgageTests {

        @Test
        @DisplayName("Should return feasible mortgage response")
        void shouldCheckMortgage() {
            final MortgageCheckRequest request = MortgageCheckRequest.builder()
                    .income(BigDecimal.valueOf(5000))
                    .loanValue(BigDecimal.valueOf(20000))
                    .homeValue(BigDecimal.valueOf(20000))
                    .maturityPeriod(10)
                    .build();

            final MortgageCheckResponse response = new MortgageCheckResponse(true, BigDecimal.valueOf(1200));
            when(mortgageService.checkMortgage(request)).thenReturn(response);

            ResponseEntity<MortgageCheckResponse> actualResponse = mortgageController.checkMortgage(request);

            assertNotNull(actualResponse);
            assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertTrue(Objects.requireNonNull(actualResponse.getBody()).feasible());
            assertThat(actualResponse.getBody().monthlyCost()).isEqualTo(BigDecimal.valueOf(1200));
            verify(mortgageService, times(1)).checkMortgage(request);
        }
    }
}
