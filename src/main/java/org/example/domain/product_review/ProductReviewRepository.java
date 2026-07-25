package org.example.domain.product_review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long>, JpaSpecificationExecutor<ProductReview> {

    boolean existsByUserIdAndProductId(Long userId, Long productId);

    Page<ProductReview> findByProductId(Long userId, Pageable page);

    @Query("""
    SELECT pr FROM ProductReview pr
    JOIN FETCH pr.product
    JOIN FETCH pr.user
    WHERE pr.id =  :reviewId
    """)
    Optional<ProductReview> findWithProductAndUser(Long reviewId);

}