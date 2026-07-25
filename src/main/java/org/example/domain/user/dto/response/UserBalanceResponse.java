package org.example.domain.user.dto.response;

import java.math.BigDecimal;

public record UserBalanceResponse(
        UserProfileResponse user,
        BigDecimal balance
) {}

