package org.example.domain.fixture.entity;

import org.example.domain.cart.Cart;
import org.example.domain.cart_item.CartItem;
import org.example.domain.product.Product;

public class CartItemFixture {

    public static Integer DEFAULT_QUANTITY = 5;

    public static CartItemBuilder builder() {
        return new CartItemBuilder();
    }

    public static class CartItemBuilder {

        private Integer quantity = DEFAULT_QUANTITY;

        private Product product = ProductFixture.builder().build();
        private Cart cart = CartFixture.builder().build();

        public CartItemBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public CartItemBuilder product(Product product) {
            this.product = product;
            return this;
        }

        public CartItemBuilder cart(Cart cart) {
            this.cart = cart;
            return this;
        }

        public CartItem build() {
            return CartItem.create(quantity, product, cart);
        }
    }


}
