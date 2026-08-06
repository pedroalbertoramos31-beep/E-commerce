package org.example.domain.product.dto.response;

import org.example.domain.category.dto.response.CategoryResponse;
import org.example.domain.product.ProductStatus;
import org.example.domain.product_stats.dto.response.ProductStatsResponse;
import org.example.domain.user.dto.response.UserSummaryResponse;

import java.math.BigDecimal;
import java.util.List;

public record ProductFoundResponse(

        Long id,
        String name,
        BigDecimal price,
        Integer stock,
        ProductStatus status,

        UserSummaryResponse vendor,

        List<CategoryResponse> categories,
        ProductStatsResponse stats

) {
}
