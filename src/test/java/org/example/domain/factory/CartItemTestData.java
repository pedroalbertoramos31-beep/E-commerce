package org.example.domain.factory;

import org.example.domain.cart.Cart;
import org.example.domain.cart_item.CartItem;
import org.example.domain.cart_item.dto.request.CartItemUpsertRequest;
import org.example.domain.product.Product;

public class CartItemTestData {

    public static Integer DEFAULT_QUANTITY = 5;

    public static CartItem simpleCartItem(Product product, Cart cart){
        return CartItem.create(DEFAULT_QUANTITY, product, cart);
    }

    public static CartItemUpsertRequest cartItemUpsertRequest(Integer quantity){
        return new CartItemUpsertRequest(quantity);
    }

}
