package io.idtx.pricing.api.domain.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRICES")
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "BRAND_ID", nullable = false)
    private Long brandId;

    @Column(name = "START_DATE", nullable = false)
    private LocalDateTime validFrom;

    @Column(name = "END_DATE", nullable = false)
    private LocalDateTime validTo;

    @Column(name = "PRICE_LIST", nullable = false)
    private Long priceListId;

    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;

    @Column(name = "PRIORITY", nullable = false)
    private Short priority;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal amount;

    @Column(name = "CURR", length = 3, nullable = false)
    private String currency;

    public Long getId() {
        return id;
    }

    public Long getBrandId() {
        return brandId;
    }

    public Price setBrandId(Long brandId) {
        this.brandId = brandId;
        return this;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public Price setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
        return this;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public Price setValidTo(LocalDateTime validTo) {
        this.validTo = validTo;
        return this;
    }

    public Long getPriceListId() {
        return priceListId;
    }

    public Price setPriceListId(Long priceListId) {
        this.priceListId = priceListId;
        return this;
    }

    public Long getProductId() {
        return productId;
    }

    public Price setProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public Short getPriority() {
        return priority;
    }

    public Price setPriority(Short priority) {
        this.priority = priority;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Price setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public Price setCurrency(String currency) {
        this.currency = currency;
        return this;
    }
}
