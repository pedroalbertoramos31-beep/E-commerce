package org.example.domain.product_review.dto.response;

import org.example.domain.user.dto.response.UserSummaryResponse;

public record ProductReviewResponse(

        Long id,
        Integer rating,
        String comment,

        Long productId,

        UserSummaryResponse user
) {
}
