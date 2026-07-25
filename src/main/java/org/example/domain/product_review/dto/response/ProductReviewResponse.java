package org.example.domain.product_review.dto.response;

import org.example.domain.user.User;

public record ProductReviewResponse(

        Long id,
        Integer rating,
        String comment,

        Long productId,

        User user
) {
}
