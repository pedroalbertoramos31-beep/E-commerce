package org.example.domain.product_category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    @Query("SELECT pc FROM ProductCategory pc JOIN FETCH pc.category WHERE pc.product.id = :productId")
    List<ProductCategory> getWithCategory(Long productId);

}
