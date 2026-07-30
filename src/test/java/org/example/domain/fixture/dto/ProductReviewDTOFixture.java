package org.example.domain.fixture.dto;

import org.example.domain.product_review.dto.request.ProductReviewCreationRequest;

public class ProductReviewDTOFixture {

    public static ProductReviewCreationRequest createReviewRequest(Integer rating, String commentary){
        return new ProductReviewCreationRequest(rating, commentary);
    }

}
