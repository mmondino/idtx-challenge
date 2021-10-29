package io.idtx.pricing.api.application.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
public class GetPriceResponseModel {

    @Schema(type="string", example = "2020-06-14T10:30:00", description = "The price is valid from this datetime. It is expressed in UTC")
    private LocalDateTime validFrom;
    @Schema(type="string", example = "2020-06-31T10:30:00", description = "The price is valid until this datetime. It is expressed in UTC")
    private LocalDateTime validTo;

    @Schema(example = "12345678", description = "Brand identifier")
    private Long brandId;
    @Schema(example = "12345678", description = "Product identifier")
    private Long productId;
    @Schema(example = "12345678", description = "Price list identifier")
    private Long priceListId;

    @Schema(example = "30.50", description = "Price amount")
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private BigDecimal amount;

    @Schema(example = "EUR", description = "Price iso currency code. See more <a target='_blank' href='https://en.wikipedia.org/wiki/ISO_4217'>https://en.wikipedia.org/wiki/ISO_4217</a>")
    private String currency;
}
