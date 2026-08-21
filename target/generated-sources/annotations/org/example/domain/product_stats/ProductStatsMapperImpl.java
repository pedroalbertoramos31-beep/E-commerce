package org.example.domain.product_stats;

import javax.annotation.processing.Generated;
import org.example.domain.product_stats.dto.response.ProductStatsResponse;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-21T12:25:35-0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.12 (Eclipse Adoptium)"
)
@Component
public class ProductStatsMapperImpl implements ProductStatsMapper {

    @Override
    public ProductStatsResponse toProductStatsResponse(ProductStats productStats) {
        if ( productStats == null ) {
            return null;
        }

        Long id = null;
        Double averageRating = null;
        Integer reviewCount = null;
        Integer salesCount = null;

        id = productStats.getId();
        averageRating = productStats.getAverageRating();
        reviewCount = productStats.getReviewCount();
        salesCount = productStats.getSalesCount();

        ProductStatsResponse productStatsResponse = new ProductStatsResponse( id, averageRating, reviewCount, salesCount );

        return productStatsResponse;
    }
}
