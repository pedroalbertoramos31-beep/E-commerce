package org.example.domain.factory;

import org.example.domain.order.Order;
import org.example.domain.user.User;

import java.math.BigDecimal;

public class OrderTestData {

    public static Order simpleOrder(User user, BigDecimal totalAmount){
        return Order.create(
                totalAmount,
                user
        );
    }

}
