package org.example.domain.fixture.entity;

import org.example.domain.order.Order;
import org.example.domain.order_item.OrderItem;
import org.example.domain.product.Product;

import java.math.BigDecimal;

public class OrderItemFixture {

    public static final Integer DEFAULT_QUANTITY = 1;
    public static final BigDecimal DEFAULT_PURCHASED_AT = BigDecimal.valueOf(99.99);

    public static OrderItemBuilder builder() {
        return new OrderItemBuilder();
    }

    public static class OrderItemBuilder {

        private Integer quantity = DEFAULT_QUANTITY;
        private BigDecimal purchasedAt = DEFAULT_PURCHASED_AT;

        private Product product = ProductFixture.builder().build();
        private Order order = OrderFixture.builder().build();

        public OrderItemBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderItemBuilder purchasedAt(BigDecimal purchasedAt) {
            this.purchasedAt = purchasedAt;
            return this;
        }

        public OrderItemBuilder product(Product product) {
            this.product = product;
            return this;
        }

        public OrderItemBuilder order(Order order) {
            this.order = order;
            return this;
        }

        public OrderItem build() {
            return OrderItem.create(product, quantity, purchasedAt, order);
        }
    }



}
