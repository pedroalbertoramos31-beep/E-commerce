package org.example.domain.product_review;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.order.OrderQuery;
import org.example.domain.product.Product;
import org.example.domain.product.ProductQuery;
import org.example.domain.product_review.dto.request.ProductReviewCreationRequest;
import org.example.domain.product_review.dto.response.ProductReviewResponse;
import org.example.domain.product_stats.ProductStats;
import org.example.domain.product_stats.ProductStatsQuery;
import org.example.domain.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductReviewService {

    private final ProductReviewRepository productReviewRepository;
    private final UserRepository userRepository;

    private final ProductReviewQuery productReviewQuery;
    private final ProductQuery productQuery;
    private final OrderQuery orderQuery;
    private final ProductStatsQuery productStatsQuery;


    private final ProductReviewMapper productReviewMapper;

    @Transactional
    public ProductReviewResponse createReview(Long productId, Long userId, ProductReviewCreationRequest request) {

        ProductStats productStats = productStatsQuery.findByProductIdWithProduct(productId);

        Product product = productStats.getProduct();

        productQuery.verifyUserIsNotOwner(product.getVendor().getId(), userId);

        orderQuery.verifyUserPurchasedProduct(userId, productId);

        productReviewQuery.verifyUserReviewedProduct(userId, productId);

        ProductReview productReview = ProductReview.create(
                request.rating(),
                request.comment(),
                product,
                userRepository.getReferenceById(userId)
        );

        ProductReview savedProductReview = productReviewRepository.save(productReview);

        productStats.updateRating(savedProductReview.getRating());

        productStats.updateReviewCount(1);

        return productReviewMapper.toProductReviewResponse(savedProductReview);

    }

    @Transactional
    public Page<ProductReviewResponse> getProductReviews(Long productId, Pageable page) {

        Page<ProductReview> reviews = productReviewRepository.findByProductId(productId, page);

        return reviews.map(productReviewMapper::toProductReviewResponse);
    }

}