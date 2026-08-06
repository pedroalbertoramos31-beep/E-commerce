package org.example.domain.product.dto.response;

import org.example.domain.product.ProductStatus;
import org.example.domain.user.dto.response.UserSummaryResponse;

import java.math.BigDecimal;

public record ProductResponse(

        Long id,
        String name,
        BigDecimal price,
        Integer stock,

        ProductStatus state,
        UserSummaryResponse vendor
) {}