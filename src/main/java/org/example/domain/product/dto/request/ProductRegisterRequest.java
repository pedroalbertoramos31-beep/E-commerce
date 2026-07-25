package org.example.domain.product.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Set;

public record ProductRegisterRequest(
    @NotBlank(message = "name cannot be blank")
    @Size(min = 3, max = 100, message = "name has to be at least 3 characters long and max 100")
    String name,

    @NotNull(message = "price is mandatory")
    @Positive(message = "price cannot be negative")
    @Digits(integer = 6, fraction = 2, message = "price has an incorrect format (max 6 integers and 2 decimals ")
    BigDecimal price,

    @NotNull(message = "is needed at least 0 stock")
    @Min(value = 0, message = "you need at least 0 stock")
    Integer stock,

    Set<Long> categoriesId

) {}