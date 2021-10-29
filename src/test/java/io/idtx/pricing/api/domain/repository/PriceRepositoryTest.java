package io.idtx.pricing.api.domain.repository;

import io.idtx.pricing.api.domain.entity.Price;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
class PriceRepositoryTest {

    @Autowired
    private PriceRepository priceRepository;

    @BeforeEach
    void beforeEach() {
        this.priceRepository.deleteAll();
    }

    @Test
    void whenNoMatchingBrandId_shouldReturnEmptyPriceList() {

        Long brandId = 1L;
        Long differentBrandId = 2L;
        Long productId = 2L;
        LocalDateTime pricingDate = LocalDateTime.parse("2021-01-07T00:00:00");

        Price price = new Price()
                .setBrandId(brandId)
                .setProductId(productId)
                .setPriceListId(3L)
                .setValidFrom(LocalDateTime.parse("2021-01-07T00:00:00"))
                .setValidTo(LocalDateTime.parse("2021-01-14T00:00:00"))
                .setPriority((short) 1)
                .setAmount(new BigDecimal("38.9"))
                .setCurrency("EUR");

        this.priceRepository.save(price);

        List<Price> prices = this.priceRepository
                .findByBrandIdAndProductIdAndPricingDate(differentBrandId, productId, pricingDate, PageRequest.of(0, 2));

        Assertions.assertEquals(0, prices.stream().count());
    }

    @Test
    void whenNoMatchingProductId_shouldReturnEmptyPriceList() {

        Long brandId = 1L;
        Long productId = 2L;
        Long differentProductId = 3L;
        LocalDateTime pricingDate = LocalDateTime.parse("2021-01-07T00:00:00");

        Price price = new Price()
                .setBrandId(brandId)
                .setProductId(productId)
                .setPriceListId(3L)
                .setValidFrom(LocalDateTime.parse("2021-01-07T00:00:00"))
                .setValidTo(LocalDateTime.parse("2021-01-14T00:00:00"))
                .setPriority((short) 1)
                .setAmount(new BigDecimal("38.9"))
                .setCurrency("EUR");

        this.priceRepository.save(price);

        List<Price> prices = this.priceRepository
                .findByBrandIdAndProductIdAndPricingDate(brandId, differentProductId, pricingDate, PageRequest.of(0, 2));

        Assertions.assertEquals(0, prices.stream().count());
    }

    @Test
    void whenPricingDateIsLessThanValidFrom_shouldReturnEmptyPriceList() {

        Long brandId = 1L;
        Long productId = 2L;
        LocalDateTime pricingDate = LocalDateTime.parse("2021-01-06T23:59:59");

        Price price = new Price()
                .setBrandId(brandId)
                .setProductId(productId)
                .setPriceListId(3L)
                .setValidFrom(LocalDateTime.parse("2021-01-07T00:00:00"))
                .setValidTo(LocalDateTime.parse("2021-01-14T00:00:00"))
                .setPriority((short) 1)
                .setAmount(new BigDecimal("38.9"))
                .setCurrency("EUR");

        this.priceRepository.save(price);

        List<Price> prices = this.priceRepository
                .findByBrandIdAndProductIdAndPricingDate(brandId, productId, pricingDate, PageRequest.of(0, 2));

        Assertions.assertEquals(0, prices.stream().count());
    }

    @Test
    void whenPricingDateIsGreaterThanValidTo_shouldReturnEmptyPriceList() {

        Long brandId = 1L;
        Long productId = 2L;
        LocalDateTime pricingDate = LocalDateTime.parse("2021-01-14T00:00:01");

        Price price = new Price()
                .setBrandId(brandId)
                .setProductId(productId)
                .setPriceListId(3L)
                .setValidFrom(LocalDateTime.parse("2021-01-07T00:00:00"))
                .setValidTo(LocalDateTime.parse("2021-01-14T00:00:00"))
                .setPriority((short) 1)
                .setAmount(new BigDecimal("38.9"))
                .setCurrency("EUR");

        this.priceRepository.save(price);

        List<Price> prices = this.priceRepository
                .findByBrandIdAndProductIdAndPricingDate(brandId, productId, pricingDate, PageRequest.of(0, 2));

        Assertions.assertEquals(0, prices.stream().count());
    }

    @Test
    void whenPricingDateIsEqualToValidFrom_shouldReturnPriceListWithOnePrice() {

        Long brandId = 1L;
        Long productId = 2L;
        LocalDateTime pricingDate = LocalDateTime.parse("2021-01-07T00:00:00");

        Price price = new Price()
                .setBrandId(brandId)
                .setProductId(productId)
                .setPriceListId(3L)
                .setValidFrom(LocalDateTime.parse("2021-01-07T00:00:00"))
                .setValidTo(LocalDateTime.parse("2021-01-14T00:00:00"))
                .setPriority((short) 1)
                .setAmount(new BigDecimal("38.9"))
                .setCurrency("EUR");

        this.priceRepository.save(price);

        List<Price> prices = this.priceRepository
                .findByBrandIdAndProductIdAndPricingDate(brandId, productId, pricingDate, PageRequest.of(0, 2));

        Assertions.assertEquals(1, prices.stream().count());
        Assertions.assertEquals(price, prices.get(0));
    }

    @Test
    void whenPricingDateIsEqualToValidTo_shouldReturnPriceListWithOnePrice() {

        Long brandId = 1L;
        Long productId = 2L;
        LocalDateTime pricingDate = LocalDateTime.parse("2021-01-14T00:00:00");

        Price price = new Price()
                .setBrandId(brandId)
                .setProductId(productId)
                .setPriceListId(3L)
                .setValidFrom(LocalDateTime.parse("2021-01-07T00:00:00"))
                .setValidTo(LocalDateTime.parse("2021-01-14T00:00:00"))
                .setPriority((short) 1)
                .setAmount(new BigDecimal("38.9"))
                .setCurrency("EUR");

        this.priceRepository.save(price);

        List<Price> prices = this.priceRepository
                .findByBrandIdAndProductIdAndPricingDate(brandId, productId, pricingDate, PageRequest.of(0, 2));

        Assertions.assertEquals(1, prices.stream().count());
        Assertions.assertEquals(price, prices.get(0));
    }

    @Test
    void whenPricingDateIsGreaterThanToValidToAndLessThanValidFrom_shouldReturnPriceListWithOnePrice() {

        Long brandId = 1L;
        Long productId = 2L;
        LocalDateTime pricingDate = LocalDateTime.parse("2021-01-07T00:00:01");

        Price price = new Price()
                .setBrandId(brandId)
                .setProductId(productId)
                .setPriceListId(3L)
                .setValidFrom(LocalDateTime.parse("2021-01-07T00:00:00"))
                .setValidTo(LocalDateTime.parse("2021-01-14T00:00:00"))
                .setPriority((short) 1)
                .setAmount(new BigDecimal("38.9"))
                .setCurrency("EUR");

        this.priceRepository.save(price);

        List<Price> prices = this.priceRepository
                .findByBrandIdAndProductIdAndPricingDate(brandId, productId, pricingDate, PageRequest.of(0, 2));

        Assertions.assertEquals(1, prices.stream().count());
        Assertions.assertEquals(price, prices.get(0));
    }

    @Test
    void whenTwoMatchingPrices_shouldReturnPriceListWithTwoPricesOrderedByPriorityDesc() {

        Long brandId = 1L;
        Long productId = 2L;
        LocalDateTime pricingDate = LocalDateTime.parse("2021-01-07T00:00:01");

        Price lowerPriorityPrice = new Price()
                .setBrandId(brandId)
                .setProductId(productId)
                .setPriceListId(3L)
                .setValidFrom(LocalDateTime.parse("2021-01-07T00:00:00"))
                .setValidTo(LocalDateTime.parse("2021-01-14T00:00:00"))
                .setPriority((short) 1)
                .setAmount(new BigDecimal("38.9"))
                .setCurrency("EUR");

        Price higherPriorityPrice = new Price()
                .setBrandId(brandId)
                .setProductId(productId)
                .setPriceListId(3L)
                .setValidFrom(LocalDateTime.parse("2021-01-07T00:00:00"))
                .setValidTo(LocalDateTime.parse("2021-01-14T00:00:00"))
                .setPriority((short) 2)
                .setAmount(new BigDecimal("41.35"))
                .setCurrency("EUR");

        this.priceRepository.saveAll(Arrays.asList(lowerPriorityPrice, higherPriorityPrice));

        List<Price> prices = this.priceRepository
                .findByBrandIdAndProductIdAndPricingDate(brandId, productId, pricingDate, PageRequest.of(0, 2));

        Assertions.assertEquals(2, prices.stream().count());
        Assertions.assertEquals(higherPriorityPrice, prices.get(0));
        Assertions.assertEquals(lowerPriorityPrice, prices.get(1));
    }
}