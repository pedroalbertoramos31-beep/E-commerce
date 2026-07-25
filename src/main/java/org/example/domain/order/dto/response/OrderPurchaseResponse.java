package org.example.domain.order.dto.response;

import org.example.domain.order_item.OrderItemResponse;
import org.example.domain.user.dto.response.UserProfileResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderPurchaseResponse(

        Long id,
        BigDecimal totalAmount,

        UserProfileResponse user,
        List<OrderItemResponse> items
) {
}
