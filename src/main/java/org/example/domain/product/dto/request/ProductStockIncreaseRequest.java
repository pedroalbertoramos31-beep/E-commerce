package org.example.domain.product.dto.request;


import jakarta.validation.constraints.Positive;

public record ProductStockIncreaseRequest(
        @Positive
        Integer quantity
) { }
