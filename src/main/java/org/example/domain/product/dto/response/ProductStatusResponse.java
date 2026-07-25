package org.example.domain.product.dto.response;

import org.example.domain.product.ProductStatus;

public record ProductStatusResponse(

        Long id,
        ProductStatus status
) {
}
