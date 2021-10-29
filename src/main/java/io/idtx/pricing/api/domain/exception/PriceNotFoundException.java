package io.idtx.pricing.api.domain.exception;

public class PriceNotFoundException extends Exception {
    public PriceNotFoundException(String message) {
        super(message);
    }
}
