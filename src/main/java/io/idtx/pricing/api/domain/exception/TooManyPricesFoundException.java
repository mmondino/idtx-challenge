package io.idtx.pricing.api.domain.exception;

public class TooManyPricesFoundException extends Exception {
    public TooManyPricesFoundException(String message) {
        super(message);
    }
}
