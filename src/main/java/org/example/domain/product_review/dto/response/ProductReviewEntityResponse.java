package org.example.domain.product_review.dto.response;

import org.example.domain.product.Product;
import org.example.domain.user.User;

public record ProductReviewEntityResponse(

        Long id,
        Integer rating,
        String comment,

        Product product,
        User user
) {
}
