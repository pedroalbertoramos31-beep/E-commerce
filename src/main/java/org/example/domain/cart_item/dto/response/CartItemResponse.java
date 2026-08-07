package org.example.domain.cart_item.dto.response;

import org.example.domain.product.dto.response.ProductCardResponse;

public record CartItemResponse(

        Long id,
        Integer quantity,

        ProductCardResponse product

        ) {
}