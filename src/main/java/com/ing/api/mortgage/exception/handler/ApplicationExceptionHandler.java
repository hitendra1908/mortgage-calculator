package com.ing.api.mortgage.exception.handler;

import com.ing.api.mortgage.exception.InvalidMaturityPeriodException;
import com.ing.api.mortgage.exception.mortgage.MortgageExceedsHomeValueException;
import com.ing.api.mortgage.exception.mortgage.MortgageExceedsIncomeLimitException;
import com.ing.api.mortgage.exception.mortgage.MortgageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(MortgageException.class)
    public ProblemDetail handleMortgageException(MortgageException exception) {
        return switch (exception) {
            case MortgageExceedsIncomeLimitException e -> createProblemDetail(e, HttpStatus.BAD_REQUEST, "Insufficient Income!!");
            case MortgageExceedsHomeValueException e -> createProblemDetail(e, HttpStatus.BAD_REQUEST, "Loan value is too high!!");
            default -> createProblemDetail(exception, HttpStatus.INTERNAL_SERVER_ERROR, "Exception while processing the mortgage request");
        };
    }

    @ExceptionHandler(InvalidMaturityPeriodException.class)
    public ProblemDetail handleInvalidMaturityPeriodException(InvalidMaturityPeriodException exception) {
        log.error("Bad Request ", exception);
        return createProblemDetail(exception, HttpStatus.BAD_REQUEST, "Maturity period not supported");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail handleNoResourceFoundException(NoResourceFoundException exception) {
        log.error("Resource not found: ", exception);
        return createProblemDetail(exception, HttpStatus.NOT_FOUND, "Resource you are trying to access not found!");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error("Bad Request ", exception);
        return createProblemDetail(exception, HttpStatus.BAD_REQUEST, "Invalid request");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("Bad Request ", exception);
        Map<String, String> errors = new HashMap<>();
        for (FieldError fe : exception.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errors.toString());
        problemDetail.setTitle("Invalid request");
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAllOtherExceptions(Exception exception) {
        log.error("Generic exception occurred while processing the request: ", exception);
        return createProblemDetail(exception, HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
    }

    private static ProblemDetail createProblemDetail(Exception exception, HttpStatus status, String title) {
        var problemDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
        problemDetail.setTitle(title);
        return problemDetail;
    }
}
