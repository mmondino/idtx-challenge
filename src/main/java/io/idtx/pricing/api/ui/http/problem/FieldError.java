package io.idtx.pricing.api.ui.http.problem;

import lombok.Getter;

@Getter
public class FieldError {

    private String path;
    private FieldErrorTypeEnum errorType;
    private String errorMessage;
    private Object rejectedValue;

    private FieldError(String path, FieldErrorTypeEnum errorType, String errorMessage, Object rejectedValue) {
        this.path = path;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.rejectedValue = rejectedValue;
    }

    public static FieldError validationError(String path, String errorMessage, Object rejectedValue) {
        return new FieldError(path, FieldErrorTypeEnum.VALIDATION_ERROR, errorMessage, rejectedValue);
    }

    public static FieldError conversionError(String path, String errorMessage, Object rejectedValue) {
        return new FieldError(path, FieldErrorTypeEnum.CONVERSION_ERROR, errorMessage, rejectedValue);
    }
}
