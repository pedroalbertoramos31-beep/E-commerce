package org.example.domain.order_item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemQuery {

    private final OrderItemRepository orderItemRepository;

    public List<OrderItem> getByOrderId(Long orderId){
        return orderItemRepository.getByOrderId(orderId);
    }

}
