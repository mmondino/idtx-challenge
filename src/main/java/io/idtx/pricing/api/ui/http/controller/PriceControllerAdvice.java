package io.idtx.pricing.api.ui.http.controller;

import io.idtx.pricing.api.application.problem.ProblemCode;
import io.idtx.pricing.api.application.problem.ProblemType;
import io.idtx.pricing.api.domain.exception.PriceNotFoundException;
import io.idtx.pricing.api.domain.exception.TooManyPricesFoundException;
import io.idtx.pricing.api.ui.http.problem.ProblemResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice(assignableTypes = {PriceController.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PriceControllerAdvice {

    @ExceptionHandler(PriceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemResponse handlePriceNotFoundException(PriceNotFoundException exception) {
        return ProblemResponse.builder()
                .timestamp(Instant.now())
                .problemType(ProblemType.RESOURCE_NOT_FOUND.name())
                .problemCode(ProblemCode.PRICE_NOT_FOUND.getCode())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(TooManyPricesFoundException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ProblemResponse handleTooManyPricesFoundException(TooManyPricesFoundException exception) {
        return ProblemResponse.builder()
                .timestamp(Instant.now())
                .problemType(ProblemType.BUSINESS_RULE_PROBLEM.name())
                .problemCode(ProblemCode.TOO_MANY_PRICES_FOUND.getCode())
                .message(exception.getMessage())
                .build();
    }
}
