package org.example.domain.product.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Set;

public record ProductFilterRequest(
        @Size(max = 100, message = "Name to search is too long")
        String name,

        @Size(max = 50, message = "Category name is too long")
        Set<String> categories,

        @DecimalMin(value = "0.0", message = "Minimum price cannot be below 0.0")
        @Digits(integer = 8, fraction = 2, message = "Invalid minimum price format")
        BigDecimal minPrice,

        @DecimalMin(value = "0.0", message = "Maximum price cannot be below 0.0")
        @Digits(integer = 8, fraction = 2, message = "Invalid maximum price format")
        BigDecimal maxPrice,

        @Min(1) @Max(5)
        Integer stars
) {}
