package org.example.domain.order_item;

import org.example.domain.product.dto.response.ProductResponse;

import java.math.BigDecimal;

public record OrderItemResponse(

        Long id,
        Integer quantity,
        BigDecimal purchasedAt,

        ProductResponse product

) {

}
