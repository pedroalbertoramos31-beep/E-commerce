package org.example.domain.order.dto.response;

import org.example.domain.order.OrderStatus;
import org.example.domain.user.User;

import java.math.BigDecimal;

public record OrderEntityResponse(
        Long id,
        BigDecimal totalAmount,
        OrderStatus status,

        User user
) {
}
