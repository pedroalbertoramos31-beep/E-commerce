package org.example.domain.order;

import org.example.domain.order.dto.response.OrderResponse;
import org.example.domain.order.dto.response.OrderPurchaseResponse;
import org.example.domain.order_item.OrderItem;
import org.example.domain.order_item.OrderItemMapper;
import org.example.domain.user.User;
import org.example.domain.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, UserMapper.class})
public interface OrderMapper {

    @Mapping(source = "order.id", target = "id")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "orderItems", target = "items")
    OrderPurchaseResponse toPurchaseResponse(Order order, List<OrderItem> orderItems, User user);

    OrderResponse toHistoryResponse(Order order);

    List<OrderResponse> toHistoryResponseList(List<Order> orders);

}
