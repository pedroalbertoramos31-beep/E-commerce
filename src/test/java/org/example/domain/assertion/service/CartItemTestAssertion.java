package org.example.domain.assertion.service;

import org.example.domain.cart_item.CartItem;
import org.example.domain.cart_item.dto.response.CartItemResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class CartItemTestAssertion {

    public static void assertCartItemResponse (CartItemResponse expectedResponse, CartItemResponse response){

        assertThat(expectedResponse.productId()).isEqualTo(response.productId());

        assertThat(expectedResponse.quantity()).isEqualTo(response.quantity());

    }

    public static void assertCartItemPersistence (CartItem expectedPersistence, CartItem item){

        assertThat(expectedPersistence.getCart().getId()).isEqualTo(item.getCart().getId());

        assertThat(expectedPersistence.getProduct().getId()).isEqualTo(item.getProduct().getId());

        assertThat(expectedPersistence.getQuantity()).isEqualTo(item.getQuantity());


    }
}
