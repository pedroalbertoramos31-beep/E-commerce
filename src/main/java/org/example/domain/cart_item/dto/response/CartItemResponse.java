package org.example.domain.cart_item.dto.response;

import org.example.domain.cart.Cart;
import org.example.domain.product.Product;

public record CartItemResponse(

        Long id,
        Integer quantity,

        Product product,
        Cart cart

        ) {
}