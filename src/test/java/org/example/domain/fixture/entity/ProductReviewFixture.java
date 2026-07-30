package org.example.domain.fixture.entity;

import org.example.domain.product.Product;
import org.example.domain.product_review.ProductReview;
import org.example.domain.user.User;

public class ProductReviewFixture {


    public static final Integer DEFAULT_RATING = 5;
    public static final String DEFAULT_COMMENT = "Great product";

    public static ProductReviewBuilder builder() {
        return new ProductReviewBuilder();
    }

    public static class ProductReviewBuilder {

        private Integer rating = DEFAULT_RATING;
        private String comment = DEFAULT_COMMENT;

        private Product product = ProductFixture.builder().build();
        private User user = UserFixture.builder().build();

        public ProductReviewBuilder rating(Integer rating) {
            this.rating = rating;
            return this;
        }

        public ProductReviewBuilder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public ProductReviewBuilder product(Product product) {
            this.product = product;
            return this;
        }

        public ProductReviewBuilder user(User user) {
            this.user = user;
            return this;
        }

        public ProductReview build() {
            return ProductReview.create(rating, comment, product, user);
        }
    }

}
