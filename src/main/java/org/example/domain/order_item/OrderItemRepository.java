package org.example.domain.order_item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository <OrderItem, Long> {

    @Query("SELECT oi FROM OrderItem oi WHERE order.id = :orderId")
    List<OrderItem> getByOrderId(Long orderId);

}
