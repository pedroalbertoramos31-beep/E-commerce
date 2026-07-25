package org.example.domain.product_review;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.audit.Auditable;
import org.example.domain.product.Product;
import org.example.domain.user.User;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "reviews",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_product_review",
                        columnNames = {"user_id", "product_id"}
                )
        }
)
public class ProductReview extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public static ProductReview create(Integer rating, String comment, Product product, User user){

        ProductReview review = new ProductReview();

        review.rating = rating;
        review.comment = comment;
        review.product = product;
        review.user = user;

        return review;
    }

}
