package io.idtx.pricing.api.application.service;

import io.idtx.pricing.api.domain.entity.Price;
import io.idtx.pricing.api.domain.exception.PriceNotFoundException;
import io.idtx.pricing.api.domain.exception.TooManyPricesFoundException;
import io.idtx.pricing.api.domain.service.PricingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class GetPriceServiceTest {

    @Mock
    private PricingService pricingService;

    private GetPriceService getPriceService;

    @BeforeEach
    void beforeEach() {
        this.getPriceService = new GetPriceService(pricingService);
    }

    @Test
    public void whenOneMatchingPrice_shouldReturnResponseModel() throws TooManyPricesFoundException, PriceNotFoundException {

        Long brandId = 1L;
        Long productId = 2L;
        LocalDateTime pricingDate = LocalDateTime.parse("2021-01-07T00:00:00");

        // First price
        Price firstPrice = new Price();
        firstPrice.setBrandId(brandId);
        firstPrice.setProductId(productId);
        firstPrice.setPriceListId(3L);
        firstPrice.setValidFrom(LocalDateTime.parse("2021-01-01T00:00:00"));
        firstPrice.setValidTo(LocalDateTime.parse("2021-01-15T23:59:59"));
        firstPrice.setPriority((short) 1);
        firstPrice.setAmount(new BigDecimal("38.5"));
        firstPrice.setCurrency("EUR");

        Mockito.when(this.pricingService.getPriceByBrandAndProductAndPricingDate(brandId, productId, pricingDate))
                .thenReturn(Optional.of(firstPrice));

        GetPriceRequestModel request = GetPriceRequestModel.builder()
                .brandId(brandId)
                .productId(productId)
                .pricingDate(pricingDate)
                .build();

        GetPriceResponseModel response = this.getPriceService.invoke(request);

        Assertions.assertEquals(firstPrice.getBrandId(), response.getBrandId());
        Assertions.assertEquals(firstPrice.getProductId(), response.getProductId());
        Assertions.assertEquals(firstPrice.getPriceListId(), response.getPriceListId());
        Assertions.assertEquals(firstPrice.getValidFrom(), response.getValidFrom());
        Assertions.assertEquals(firstPrice.getValidTo(), response.getValidTo());
        Assertions.assertEquals(firstPrice.getAmount(), response.getAmount());
        Assertions.assertEquals(firstPrice.getCurrency(), response.getCurrency());
    }

    @Test
    public void whenNoMatchingPrice_shouldThrowsPriceNotFoundException() throws TooManyPricesFoundException {

        Long brandId = 1L;
        Long productId = 2L;
        LocalDateTime pricingDate = LocalDateTime.parse("2021-01-07T00:00:00");

        Mockito.when(this.pricingService.getPriceByBrandAndProductAndPricingDate(brandId, productId, pricingDate))
                .thenReturn(Optional.ofNullable(null));

        GetPriceRequestModel request = GetPriceRequestModel.builder()
                .brandId(brandId)
                .productId(productId)
                .pricingDate(pricingDate)
                .build();

        assertThrows(PriceNotFoundException.class, () -> this.getPriceService.invoke(request));
    }

    @Test
    public void whenTwoMatchingPrices_shouldThrowsTooManyPricesFoundException() throws TooManyPricesFoundException {

        Long brandId = 1L;
        Long productId = 2L;
        LocalDateTime pricingDate = LocalDateTime.parse("2021-01-07T00:00:00");

        Mockito.when(this.pricingService.getPriceByBrandAndProductAndPricingDate(brandId, productId, pricingDate))
                .thenThrow(TooManyPricesFoundException.class);

        GetPriceRequestModel request = GetPriceRequestModel.builder()
                .brandId(brandId)
                .productId(productId)
                .pricingDate(pricingDate)
                .build();

        assertThrows(TooManyPricesFoundException.class, () -> this.getPriceService.invoke(request));
    }
}