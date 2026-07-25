package org.example.domain.cart.dto.response;

import org.example.domain.cart_item.dto.response.CartItemResponse;

import java.util.List;

public record CartItemsResponse(

        Long id,

        List<CartItemResponse> items
) {
}
