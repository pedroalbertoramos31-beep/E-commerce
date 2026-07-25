package org.example.domain.product_review;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.example.domain.product_review.dto.request.ProductReviewCreationRequest;
import org.example.domain.product_review.dto.response.ProductReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product/review")
public class ProductReviewController {

    private final ProductReviewService productReviewService;

    @PostMapping("/{productId}")
    public ResponseEntity<ProductReviewResponse> createReview(
            @PathVariable Long productId,
            @Valid @RequestBody ProductReviewCreationRequest request,
            @AuthenticationPrincipal(expression = "id") Long userId) {

        ProductReviewResponse review = productReviewService.createReview(productId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Page<ProductReviewResponse>> getProductReviews(
            @PathVariable @Positive Long productId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt") Pageable pageable)
    {

        Page<ProductReviewResponse> reviews = productReviewService.getProductReviews(productId, pageable);

        return ResponseEntity.ok(reviews);
    }


}
