package io.idtx.pricing.api.ui.http.controller;

import io.idtx.pricing.api.application.problem.ProblemType;
import io.idtx.pricing.api.ui.http.problem.FieldError;
import io.idtx.pricing.api.ui.http.problem.InvalidRequestResponse;
import io.idtx.pricing.api.ui.http.problem.ProblemResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalControllerAdvice {

    private final MessageSource messageSource;

    @Autowired
    public GlobalControllerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected InvalidRequestResponse handleBindException(BindException exception) {

        // Global errors
        List<String> globalErrors = new ArrayList();

        for (ObjectError error : exception.getBindingResult().getGlobalErrors()) {
            globalErrors.add(error.getDefaultMessage());
        }

        // Field errors
        List<FieldError> fieldErrors = new ArrayList();

        for (org.springframework.validation.FieldError error : exception.getBindingResult().getFieldErrors()) {

            String fieldErrorMessage = this.messageSource.getMessage(
                    new DefaultMessageSourceResolvable(
                            error.getCodes(),
                            error.getArguments(),
                            error.getDefaultMessage()),
                    Locale.getDefault());

            if (error.isBindingFailure()) {
                fieldErrors.add(FieldError.conversionError(error.getField(), fieldErrorMessage, error.getRejectedValue()));
            } else {
                fieldErrors.add(FieldError.validationError(error.getField(), fieldErrorMessage, error.getRejectedValue()));
            }
        }

        return InvalidRequestResponse.builder()
                .timestamp(Instant.now())
                .problemType(ProblemType.INVALID_REQUEST.name())
                .message("The request is not valid")
                .globalErrors(globalErrors)
                .fieldErrors(fieldErrors).build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemResponse handleException(Exception exception) {

        log.error("Uncaught exception", exception);

        return ProblemResponse.builder()
                .timestamp(Instant.now())
                .problemType(ProblemType.API_ERROR.name())
                .build();
    }
}
