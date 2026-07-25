package org.example.domain.product_stats;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductStatsRepository extends JpaRepository<ProductStats, Long>, JpaSpecificationExecutor<ProductStats> {

    @Query("SELECT ps FROM ProductStats ps JOIN FETCH ps.product WHERE ps.product.id = :productId")
    Optional<ProductStats> findProductStatsByIdWithProduct(Long productId);

    List<ProductStats> findByProductIdIn(List<Long> productIds);

    @Query("""
        SELECT ps FROM ProductStats ps 
        JOIN FETCH ps.product p 
        WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))
        """)
    Page<ProductStats> getProductLikeName(Pageable page, String name);


}
