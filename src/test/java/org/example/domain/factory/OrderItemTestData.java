package org.example.domain.factory;

import org.example.domain.order.Order;
import org.example.domain.order_item.OrderItem;
import org.example.domain.product.Product;

import java.math.BigDecimal;

public class OrderItemTestData {

    public static OrderItem simpleOrderItem(Product product, Order order, BigDecimal subtotal){
        return OrderItem.create(
                product,
                1,
                subtotal,
                order
        );

    }

}
