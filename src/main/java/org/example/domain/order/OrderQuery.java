package org.example.domain.order;

import lombok.RequiredArgsConstructor;
import org.example.domain.order_item.OrderItemRepository;
import org.example.infrastructure.exception.error.OrderException;
import org.example.infrastructure.exception.error.ReviewException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderQuery {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public List<Order> getByUserId(Long userId){
        return orderRepository.findAllByUserId(userId);
    }

    public Order findByIdAndUserId(Long orderId, Long userId){
        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(OrderException.OrderNotFound::new);
    }

    public void verifyUserPurchasedProduct(Long userId, Long productId){
        if (!orderRepository.verifyUserPurchasedProduct(userId, productId, OrderStatus.BOUGHT)) {
            throw new ReviewException.ProductNotBought();
        }
    }


}


