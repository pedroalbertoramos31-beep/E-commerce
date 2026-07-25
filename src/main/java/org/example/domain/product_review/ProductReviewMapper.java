package org.example.domain.product_review;

import org.example.domain.product_review.dto.response.ProductReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductReviewMapper {

    @Mapping(source = "review.product.id", target = "productId")
    ProductReviewResponse toProductReviewResponse(ProductReview review);

}
