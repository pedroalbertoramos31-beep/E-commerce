package org.example.domain.order_item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.domain.audit.Auditable;
import org.example.domain.order.Order;
import org.example.domain.product.Product;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
public class OrderItem extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal purchasedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    // CONSTRUCTOR

    public static OrderItem create(Product product, Integer quantity, BigDecimal purchasedAt, Order order){

        OrderItem orderItem = new OrderItem();

        orderItem.product = product;
        orderItem.quantity = quantity;
        orderItem.purchasedAt = purchasedAt;
        orderItem.order = order;

        return orderItem;
    }


}
