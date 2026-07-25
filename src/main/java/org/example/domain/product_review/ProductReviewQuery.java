package org.example.domain.product_review;

import lombok.RequiredArgsConstructor;
import org.example.infrastructure.exception.error.ReviewException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductReviewQuery {

    private final ProductReviewRepository productReviewRepository;

    public void verifyUserReviewedProduct(Long userId, Long productId){
        if (productReviewRepository.existsByUserIdAndProductId(userId, productId)){
            throw new ReviewException.AlreadyReviewed();
        }
    }

    public ProductReview findByIdWithProductAndUser(Long productReviewId) {
        return productReviewRepository.findWithProductAndUser(productReviewId)
                .orElseThrow(ReviewException.ReviewNotFound::new);
    }





}
