package org.example.domain.order.dto.response;

import org.example.domain.order.OrderStatus;

import java.math.BigDecimal;

public record OrderResponse(

        Long id,
        BigDecimal totalAmount,
        OrderStatus status

) {
}
