package org.example.domain.factory;

import org.example.domain.product.Product;
import org.example.domain.product_review.ProductReview;
import org.example.domain.product_review.dto.request.ProductReviewCreationRequest;
import org.example.domain.user.User;

public class ProductReviewTestData {

    public static ProductReviewCreationRequest createReviewRequest(Integer rating, String commentary){
        return new ProductReviewCreationRequest(rating, commentary);
    }

    public static ProductReview simpleProductReview(Integer rating, String comment, Product product, User user){
        return ProductReview.create(
                rating,
                comment,
                product,
                user
        );
    }


}
