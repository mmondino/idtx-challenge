package io.idtx.pricing.api.domain.service;

import io.idtx.pricing.api.domain.entity.Price;
import io.idtx.pricing.api.domain.exception.TooManyPricesFoundException;
import io.idtx.pricing.api.domain.repository.PriceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PricingServiceTest {

    @Mock
    private PriceRepository priceRepository;

    private PricingService pricingService;

    @BeforeEach
    void beforeEach() {
        this.pricingService = new PricingService(priceRepository);
    }

    @Test
    public void whenTwoMatchingPricesWithSamePriority_shouldThrowsException() {

        Long brandId = 1L;
        Long productId = 2L;

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

        // Second price
        Price secondPrice = new Price();
        secondPrice.setBrandId(brandId);
        secondPrice.setProductId(productId);
        secondPrice.setPriceListId(4L);
        secondPrice.setValidFrom(LocalDateTime.parse("2021-01-01T00:00:00"));
        secondPrice.setValidTo(LocalDateTime.parse("2021-01-15T23:59:59"));
        secondPrice.setPriority((short) 1);
        secondPrice.setAmount(new BigDecimal("40.5"));
        secondPrice.setCurrency("EUR");

        Mockito.when(this.priceRepository.findByBrandIdAndProductIdAndPricingDate(
                        brandId, productId, LocalDateTime.parse("2021-01-07T00:00:00"), PageRequest.of(0, 2)))
                .thenReturn(Arrays.asList(firstPrice, secondPrice));

        assertThrows(TooManyPricesFoundException.class, () -> this.pricingService.getPriceByBrandAndProductAndPricingDate(1L, 2L, LocalDateTime.parse("2021-01-07T00:00:00")));
    }

    @Test
    public void whenTwoPriceWithDifferentPriority_shouldReturnOptionalWithValue() throws TooManyPricesFoundException {

        Long brandId = 1L;
        Long productId = 2L;

        // First price
        Price firstPrice = new Price();
        firstPrice.setBrandId(brandId);
        firstPrice.setProductId(productId);
        firstPrice.setPriceListId(3L);
        firstPrice.setValidFrom(LocalDateTime.parse("2021-01-01T00:00:00"));
        firstPrice.setValidTo(LocalDateTime.parse("2021-01-15T23:59:59"));
        firstPrice.setPriority((short) 2);
        firstPrice.setAmount(new BigDecimal("38.5"));
        firstPrice.setCurrency("EUR");

        // Second price
        Price secondPrice = new Price();
        secondPrice.setBrandId(brandId);
        secondPrice.setProductId(productId);
        secondPrice.setPriceListId(4L);
        secondPrice.setValidFrom(LocalDateTime.parse("2021-01-01T00:00:00"));
        secondPrice.setValidTo(LocalDateTime.parse("2021-01-15T23:59:59"));
        secondPrice.setPriority((short) 1);
        secondPrice.setAmount(new BigDecimal("40.5"));
        secondPrice.setCurrency("EUR");

        Mockito.when(this.priceRepository.findByBrandIdAndProductIdAndPricingDate(
                        brandId, productId, LocalDateTime.parse("2021-01-07T00:00:00"), PageRequest.of(0, 2)))
                .thenReturn(Arrays.asList(firstPrice, secondPrice));

        Optional<Price> priceOptional = this.pricingService
                .getPriceByBrandAndProductAndPricingDate(brandId, productId, LocalDateTime.parse("2021-01-07T00:00:00"));

        Assertions.assertTrue(priceOptional.isPresent());
        Assertions.assertEquals(firstPrice, priceOptional.get());
    }

    @Test
    public void whenOneMatchingPrice_shouldReturnOptionalWithValue() throws TooManyPricesFoundException {

        Long brandId = 1L;
        Long productId = 2L;

        // First price
        Price firstPrice = new Price();
        firstPrice.setBrandId(brandId);
        firstPrice.setProductId(productId);
        firstPrice.setPriceListId(3L);
        firstPrice.setValidFrom(LocalDateTime.parse("2021-01-01T00:00:00"));
        firstPrice.setValidTo(LocalDateTime.parse("2021-01-15T23:59:59"));
        firstPrice.setPriority((short) 2);
        firstPrice.setAmount(new BigDecimal("38.5"));
        firstPrice.setCurrency("EUR");

        Mockito.when(this.priceRepository.findByBrandIdAndProductIdAndPricingDate(
                        brandId, productId, LocalDateTime.parse("2021-01-07T00:00:00"), PageRequest.of(0, 2)))
                .thenReturn(Arrays.asList(firstPrice));

        Optional<Price> priceOptional = this.pricingService
                .getPriceByBrandAndProductAndPricingDate(brandId, productId, LocalDateTime.parse("2021-01-07T00:00:00"));

        Assertions.assertTrue(priceOptional.isPresent());
        Assertions.assertEquals(firstPrice, priceOptional.get());
    }

    @Test
    public void whenNoMatchingPrice_shouldReturnOptionalWithoutValue() throws TooManyPricesFoundException {

        Long brandId = 1L;
        Long productId = 2L;

        Mockito.when(this.priceRepository.findByBrandIdAndProductIdAndPricingDate(
                        brandId, productId, LocalDateTime.parse("2021-01-07T00:00:00"), PageRequest.of(0, 2)))
                .thenReturn(Arrays.asList());

        Optional<Price> priceOptional = this.pricingService
                .getPriceByBrandAndProductAndPricingDate(brandId, productId, LocalDateTime.parse("2021-01-07T00:00:00"));

        Assertions.assertFalse(priceOptional.isPresent());
    }
}