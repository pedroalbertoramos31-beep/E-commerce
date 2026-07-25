package org.example.domain.order_item.dto.response;

import org.example.domain.order.Order;
import org.example.domain.product.Product;

import java.math.BigDecimal;

public record OrderItemEntityResponse(
        Long id,
        Integer quantity,
        BigDecimal purchasedAt,
        BigDecimal subtotal,

        Product product,
        Order order
) {
}
