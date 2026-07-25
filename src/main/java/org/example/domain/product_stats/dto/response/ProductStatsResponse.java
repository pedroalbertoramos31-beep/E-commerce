package org.example.domain.product_stats.dto.response;

public record ProductStatsResponse(

        Long id,
        Double averageRating,
        Integer reviewCount,
        Integer salesCount
) {
}
