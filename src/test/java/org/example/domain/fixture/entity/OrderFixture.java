package org.example.domain.fixture.entity;

import org.example.domain.order.Order;
import org.example.domain.user.User;

import java.math.BigDecimal;

public class OrderFixture {

    public static final BigDecimal DEFAULT_TOTAL_AMOUNT = BigDecimal.valueOf(100);

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public static class OrderBuilder {

        private BigDecimal totalAmount = DEFAULT_TOTAL_AMOUNT;
        private User user = UserFixture.builder().build();

        public OrderBuilder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public OrderBuilder user(User user) {
            this.user = user;
            return this;
        }

        public Order build() {
            return Order.create(totalAmount, user);
        }
    }



}
