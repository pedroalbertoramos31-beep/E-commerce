package org.example.domain.cart_item.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CartItemQuantityRequest(
        @Positive
        int quantity) {
}
