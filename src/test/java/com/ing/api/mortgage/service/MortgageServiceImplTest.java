package com.ing.api.mortgage.service;

import com.ing.api.mortgage.dto.MortgageCheckRequest;
import com.ing.api.mortgage.dto.MortgageCheckResponse;
import com.ing.api.mortgage.dto.MortgageRateDto;
import com.ing.api.mortgage.exception.InvalidMaturityPeriodException;
import com.ing.api.mortgage.exception.mortgage.MortgageExceedsHomeValueException;
import com.ing.api.mortgage.exception.mortgage.MortgageExceedsIncomeLimitException;
import com.ing.api.mortgage.model.MortgageRate;
import com.ing.api.mortgage.repository.MortgageRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MortgageServiceImplTest {

    @Mock
    private MortgageRateRepository mortgageRateRepository;

    @InjectMocks
    private MortgageServiceImpl mortgageService;

    private MortgageRate mortgageRate10Y;
    private MortgageRate mortgageRate20Y;

    @BeforeEach
    void setUp() {
        mortgageRate10Y = MortgageRate.builder()
                .interestRate(BigDecimal.valueOf(5.0))
                .maturityPeriod(10)
                .lastUpdate(Instant.now())
                .build();

        mortgageRate20Y = MortgageRate.builder()
                .interestRate(BigDecimal.valueOf(3.50))
                .maturityPeriod(20)
                .lastUpdate(Instant.now())
                .build();
    }

    @Test
    @DisplayName("It should return all the possible mortgage rates.")
    void should_return_All_MortgageRates() {
        when(mortgageRateRepository.findAll()).thenReturn(List.of(mortgageRate10Y, mortgageRate20Y));

        List<MortgageRateDto> result = mortgageService.getAllMortgageRates();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.getFirst().interestRate()).isEqualTo(BigDecimal.valueOf(5.0));
        assertThat(result.getFirst().maturityPeriod()).isEqualTo(10);
        assertThat(result.get(1).interestRate()).isEqualTo(BigDecimal.valueOf(3.5));
        assertThat(result.get(1).maturityPeriod()).isEqualTo(20);
        verify(mortgageRateRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Given valid mortgage check request, it should return valid response with monthly cost ")
    void should_returnValidResponse_for_checkMortgage() {
        final MortgageCheckRequest request = MortgageCheckRequest.builder()
                        .name("test-user")
                        .maturityPeriod(10)
                        .income(BigDecimal.valueOf(80000))
                        .loanValue(BigDecimal.valueOf(200000))
                        .homeValue(BigDecimal.valueOf(250000))
                        .build();

        when(mortgageRateRepository.findById(10)).thenReturn(Optional.of(mortgageRate10Y));

        MortgageCheckResponse response = mortgageService.checkMortgage(request);

        assertTrue(response.feasible());
        assertNotNull(response.monthlyCost());
        assertThat(response.monthlyCost()).isEqualTo(BigDecimal.valueOf(2121.31));
        verify(mortgageRateRepository).findById(10);
    }

    @Test
    void throwsException_when_loanExceedsIncome() {
        final MortgageCheckRequest request = MortgageCheckRequest.builder()
                .maturityPeriod(10)
                .income(BigDecimal.valueOf(80000))
                .loanValue(BigDecimal.valueOf(2000000))
                .homeValue(BigDecimal.valueOf(250000))
                .build();

        assertThrows(MortgageExceedsIncomeLimitException.class, () -> mortgageService.checkMortgage(request));
        verifyNoInteractions(mortgageRateRepository);
    }

    @Test
    void throwsException_when_loanExceedsHomeValue() {
        final MortgageCheckRequest request = MortgageCheckRequest.builder()
                .maturityPeriod(10)
                .income(BigDecimal.valueOf(80000))
                .loanValue(BigDecimal.valueOf(20000))
                .homeValue(BigDecimal.valueOf(2500))
                .build();

        assertThrows(MortgageExceedsHomeValueException.class, () -> mortgageService.checkMortgage(request));
        verifyNoInteractions(mortgageRateRepository);
    }

    @Test
    void throwsException_when_invalidMaturityPeriod() {
        final MortgageCheckRequest request = MortgageCheckRequest.builder()
                .maturityPeriod(30)
                .income(BigDecimal.valueOf(80000))
                .loanValue(BigDecimal.valueOf(2000))
                .homeValue(BigDecimal.valueOf(2500))
                .build();

        when(mortgageRateRepository.findById(30)).thenReturn(Optional.empty());

        assertThrows(InvalidMaturityPeriodException.class, () -> mortgageService.checkMortgage(request));
        verify(mortgageRateRepository).findById(30);
    }

}
