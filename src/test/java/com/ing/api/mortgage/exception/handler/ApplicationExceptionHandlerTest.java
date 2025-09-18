package com.ing.api.mortgage.exception.handler;

import com.ing.api.mortgage.exception.InvalidMaturityPeriodException;
import com.ing.api.mortgage.exception.mortgage.MortgageExceedsHomeValueException;
import com.ing.api.mortgage.exception.mortgage.MortgageExceedsIncomeLimitException;
import com.ing.api.mortgage.exception.mortgage.MortgageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationExceptionHandlerTest {

    private ApplicationExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ApplicationExceptionHandler();
    }

    @Test
    void should_HandleMortgageException_for_MaxIncomeLimit() {
        MortgageException exception = new MortgageExceedsIncomeLimitException(BigDecimal.valueOf(10000), BigDecimal.valueOf(100));
        ProblemDetail detail = handler.handleMortgageException(exception);

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(detail.getTitle()).isEqualTo("Insufficient Income!!");
        assertThat(detail.getDetail()).isEqualTo("Mortgage value of 10000 exceeds the allowed limit of 4x the income: 100");
    }

    @Test
    void should_HandleMortgageException_for_LoanExceedHomeValue() {
        MortgageException exception = new MortgageExceedsHomeValueException(BigDecimal.valueOf(10000), BigDecimal.valueOf(1000));
        ProblemDetail detail = handler.handleMortgageException(exception);

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(detail.getTitle()).isEqualTo("Loan value is too high!!");
        assertThat(detail.getDetail()).isEqualTo("Mortgage value of 10000 exceeds the total home value: 1000");
    }

    @Test
    void should_HandleMortgageException_GenericMortgageException() {
        MortgageException exception = new MortgageException("Some mortgage error");
        ProblemDetail detail = handler.handleMortgageException(exception);

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(detail.getTitle()).isEqualTo("Exception while processing the mortgage request");
        assertThat(detail.getDetail()).isEqualTo("Some mortgage error");
    }


    @Test
    void should_HandleInvalidMaturityPeriodException() {
        InvalidMaturityPeriodException exception = new InvalidMaturityPeriodException(6);
        ProblemDetail detail = handler.handleInvalidMaturityPeriodException(exception);

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(detail.getTitle()).isEqualTo("Maturity period not supported");
        assertThat(detail.getDetail()).isEqualTo("Maturity period 6 is invalid");
    }

    @Test
    void should_HandleNoResourceFoundException() {
        NoResourceFoundException exception = new NoResourceFoundException(HttpMethod.GET, "/not-found");
        ProblemDetail detail = handler.handleNoResourceFoundException(exception);

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(detail.getTitle()).isEqualTo("Resource you are trying to access not found!");
        assertThat(detail.getDetail()).isEqualTo("No static resource /not-found.");
    }

    @Test
    void should_HandleHttpMessageNotReadableException() {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Malformed JSON");
        ProblemDetail detail = handler.handleHttpMessageNotReadableException(exception);

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(detail.getTitle()).isEqualTo("Invalid request");
        assertThat(detail.getDetail()).isEqualTo("Malformed JSON");
    }

    @Test
    void testHandleAllOtherExceptions() {
        Exception ex = new Exception("Unexpected failure");
        ProblemDetail detail = handler.handleAllOtherExceptions(ex);

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(detail.getTitle()).isEqualTo("Something went wrong!");
        assertThat(detail.getDetail()).isEqualTo("Unexpected failure");
    }
}
