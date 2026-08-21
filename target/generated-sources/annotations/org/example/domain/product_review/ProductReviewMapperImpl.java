package org.example.domain.product_review;

import javax.annotation.processing.Generated;
import org.example.domain.product.Product;
import org.example.domain.product_review.dto.response.ProductReviewResponse;
import org.example.domain.user.User;
import org.example.domain.user.dto.response.UserSummaryResponse;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-21T12:25:35-0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.12 (Eclipse Adoptium)"
)
@Component
public class ProductReviewMapperImpl implements ProductReviewMapper {

    @Override
    public ProductReviewResponse toProductReviewResponse(ProductReview review) {
        if ( review == null ) {
            return null;
        }

        Long productId = null;
        Long id = null;
        Integer rating = null;
        String comment = null;
        UserSummaryResponse user = null;

        productId = reviewProductId( review );
        id = review.getId();
        rating = review.getRating();
        comment = review.getComment();
        user = userToUserSummaryResponse( review.getUser() );

        ProductReviewResponse productReviewResponse = new ProductReviewResponse( id, rating, comment, productId, user );

        return productReviewResponse;
    }

    private Long reviewProductId(ProductReview productReview) {
        Product product = productReview.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    protected UserSummaryResponse userToUserSummaryResponse(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String username = null;

        id = user.getId();
        username = user.getUsername();

        UserSummaryResponse userSummaryResponse = new UserSummaryResponse( id, username );

        return userSummaryResponse;
    }
}
