package org.example.domain.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.audit.Auditable;
import org.example.domain.user.User;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /* CONSTRUCTOR */

    public static Order create(BigDecimal totalAmount, User user){

        Order order = new Order();

        order.totalAmount = totalAmount;
        order.status = OrderStatus.BOUGHT;
        order.user = user;

        return order;

    }

    /* METHODS */


}
