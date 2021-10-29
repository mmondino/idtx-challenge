package io.idtx.pricing.api.domain.repository;

import io.idtx.pricing.api.domain.entity.Price;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

    @Query(
            value = "SELECT " +
                    "  p " +
                    "FROM " +
                    "  Price p " +
                    "WHERE " +
                    "  p.brandId = :brandId AND " +
                    "  p.productId = :productId AND " +
                    "  p.validFrom <= :pricingDate AND " +
                    "  p.validTo >= :pricingDate " +
                    "ORDER BY " +
                    "  p.priority DESC")
    List<Price> findByBrandIdAndProductIdAndPricingDate(
            @Param("brandId") Long brandId,
            @Param("productId") Long productId,
            @Param("pricingDate") LocalDateTime pricingDate,
            Pageable pageRequest);
}
