package org.example.domain.cart_item.dto.response;

public record CartItemResponse(

        Long id,
        Long productId,
        Integer quantity

        ) {
}