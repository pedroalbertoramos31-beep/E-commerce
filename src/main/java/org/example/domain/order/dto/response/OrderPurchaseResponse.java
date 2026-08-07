package org.example.domain.order.dto.response;

import org.example.domain.order_item.OrderItemResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderPurchaseResponse(

        Long id,
        BigDecimal totalAmount,

        List<OrderItemResponse> items
) {
}
