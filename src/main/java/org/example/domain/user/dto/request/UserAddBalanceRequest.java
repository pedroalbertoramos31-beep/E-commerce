package org.example.domain.user.dto.request;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UserAddBalanceRequest(

        @Positive
        BigDecimal balance
) {
}
