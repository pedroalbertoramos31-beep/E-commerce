package org.example.domain.product;

import lombok.RequiredArgsConstructor;
import org.example.domain.product_category.ProductCategoryRepository;
import org.example.domain.product_review.ProductReviewRepository;
import org.example.domain.product_stats.ProductStatsRepository;
import org.example.infrastructure.exception.error.ProductException;
import org.example.infrastructure.exception.error.ReviewException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductQuery {

    private final ProductRepository productRepository;
    private final ProductStatsRepository productStatsRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductReviewRepository productReviewRepository;

    public Product findById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException.NotFound(productId));
    }

    public void verifyExistsByName(String name) {
        if (productRepository.existsByName(name)) {
            throw new ProductException.DuplicateName(name);
        }
    }

    public void verifyUserIsOwner(Long vendorId, Long userId){
        if (!vendorId.equals(userId)){
            throw new ProductException.IllegalResourceAccess();
        }
    }

    public void verifyUserIsNotOwner(Long vendorId, Long userId){
        if (vendorId.equals(userId)){
            throw new ReviewException.SelfReview();
        }
    }

    public void verifyAvailableStock(Integer productQuantity, Integer requestedQuantity){
        if (requestedQuantity > productQuantity){
            throw new ProductException.InsufficientStock();
        }
    }

}