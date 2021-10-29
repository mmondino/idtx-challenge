package io.idtx.pricing.api.application.problem;

public enum ProblemCode {

    PRICE_NOT_FOUND("PRICES.PRICE_NOT_FOUND"),
    TOO_MANY_PRICES_FOUND("PRICES.TOO_MANY_PRICES_FOUND");

    private String code;

    private ProblemCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
