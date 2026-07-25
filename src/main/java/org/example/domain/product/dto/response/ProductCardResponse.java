package org.example.domain.product.dto.response;

import org.example.domain.product_stats.dto.response.ProductStatsResponse;

public record ProductCardResponse(

        Long id,
        String name,

        ProductStatsResponse stats
) {}
