package org.example.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUserId(Long userId);

    @Query("""
        SELECT COUNT(oi) > 0 
        FROM OrderItem oi 
        JOIN oi.order o 
        WHERE o.user.id = :userId 
          AND oi.product.id = :productId 
          AND o.status = :status
    """)
    Boolean verifyUserPurchasedProduct(Long userId, Long productId, OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.user.id = :userId")
    Optional<Order> findByIdAndUserId(Long orderId, Long userId);
}
