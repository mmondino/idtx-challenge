package io.idtx.pricing.api.domain.service;

import io.idtx.pricing.api.domain.entity.Price;
import io.idtx.pricing.api.domain.exception.TooManyPricesFoundException;
import io.idtx.pricing.api.domain.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PricingService {

    private final PriceRepository priceRepository;

    @Autowired
    public PricingService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Price> getPriceByBrandAndProductAndPricingDate(Long brandId, Long productId, LocalDateTime pricingDate) throws TooManyPricesFoundException {

        List<Price> prices = this.priceRepository.findByBrandIdAndProductIdAndPricingDate(
                brandId,
                productId,
                pricingDate,
                PageRequest.of(0, 2)
        );

        if (prices.size() > 1) {

            Price firstPrice = prices.get(0);
            Price secondPrice = prices.get(1);

            if (firstPrice.getPriority().equals(secondPrice.getPriority())) {
                throw new TooManyPricesFoundException("Too many prices found. Ambiguous price");
            }
        }

        return prices.stream().findFirst();
    }
}
