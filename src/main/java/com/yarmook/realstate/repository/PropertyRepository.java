package com.yarmook.realstate.repository;

import com.yarmook.realstate.domain.Property;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Property entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PropertyRepository extends MongoRepository<Property, Long> {
    Optional<Property> findOneByExtId(Long extId);

    List<Property> findByMinPriceAedBetween(BigDecimal min, BigDecimal max, Pageable pageable);

    List<Property> findByMinPriceAedLessThanEqual(BigDecimal max, Pageable pageable);

    List<Property> findByMinPriceAedGreaterThanEqual(BigDecimal min, Pageable pageable);

    List<Property> findByPriceCurrencyIgnoreCaseAndMinPriceBetween(String currency, BigDecimal min, BigDecimal max, Pageable pageable);

    List<Property> findByPriceCurrencyIgnoreCaseAndMinPriceLessThanEqual(String currency, BigDecimal max, Pageable pageable);

    List<Property> findByPriceCurrencyIgnoreCaseAndMinPriceGreaterThanEqual(String currency, BigDecimal min, Pageable pageable);
}
