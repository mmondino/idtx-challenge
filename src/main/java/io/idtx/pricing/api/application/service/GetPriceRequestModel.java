package io.idtx.pricing.api.application.service;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter
public class GetPriceRequestModel {

    @Schema(example = "12345678", description = "Brand identifier")
    @NotNull
    private Long brandId;

    @Schema(example = "12345678", description = "Product identifier")
    @NotNull
    private Long productId;

    @Schema(type="string", example = "2020-06-14T10:30:00", description = "Pricing datetime. Expressed in UTC")
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime pricingDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetPriceRequestModel that = (GetPriceRequestModel) o;
        return Objects.equals(pricingDate, that.pricingDate) && Objects.equals(brandId, that.brandId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pricingDate, brandId, productId);
    }
}
