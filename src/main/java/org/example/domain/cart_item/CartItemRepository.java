package org.example.domain.cart_item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByProductIdAndCartId(Long productId, Long cartId);


    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM CartItem ci WHERE ci.product.id = :productId AND ci.cart.id = :cartId ")
    void deleteItem(Long productId, Long cartId);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.id IN :ids")
    void deleteItems(List<Long> ids);

    @Query("""
    SELECT ci FROM CartItem ci
    JOIN FETCH ci.product p
    JOIN ci.cart c           \s
    WHERE c.id = :userId \s
""")
    List<CartItem> getAllCartItems(@Param("userId") Long userId);
}

