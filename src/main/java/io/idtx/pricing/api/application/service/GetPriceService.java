package io.idtx.pricing.api.application.service;

import io.idtx.pricing.api.domain.entity.Price;
import io.idtx.pricing.api.domain.exception.PriceNotFoundException;
import io.idtx.pricing.api.domain.exception.TooManyPricesFoundException;
import io.idtx.pricing.api.domain.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetPriceService {

    private final PricingService pricingService;

    @Autowired
    public GetPriceService(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    public GetPriceResponseModel invoke(GetPriceRequestModel request) throws PriceNotFoundException, TooManyPricesFoundException {

        Optional<Price> priceOptional = this.pricingService.getPriceByBrandAndProductAndPricingDate(
                request.getBrandId(),
                request.getProductId(),
                request.getPricingDate()
        );

        if (!priceOptional.isPresent()) {
            throw new PriceNotFoundException("Price not found");
        }

        return this.createResponseModel(priceOptional.get());
    }

    private GetPriceResponseModel createResponseModel(Price price) {

        return GetPriceResponseModel.builder()
                .brandId(price.getBrandId())
                .productId(price.getProductId())
                .priceListId(price.getPriceListId())
                .validFrom(price.getValidFrom())
                .validTo(price.getValidTo())
                .amount(price.getAmount())
                .currency(price.getCurrency())
                .build();
    }
}
