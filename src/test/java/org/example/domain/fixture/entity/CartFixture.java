package org.example.domain.fixture.entity;

import org.example.domain.cart.Cart;
import org.example.domain.user.User;

public class CartFixture {

    public static CartBuilder builder() {
        return new CartBuilder();
    }

    public static class CartBuilder {

        private User user = UserFixture.builder().build();

        public CartBuilder user(User user) {
            this.user = user;
            return this;
        }

        public Cart build() {
            return Cart.create(user);
        }
    }

}
