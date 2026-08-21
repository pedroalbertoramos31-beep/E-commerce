package org.example.domain.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.domain.order.dto.response.OrderPurchaseResponse;
import org.example.domain.order.dto.response.OrderResponse;
import org.example.domain.order_item.OrderItem;
import org.example.domain.order_item.OrderItemMapper;
import org.example.domain.order_item.OrderItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-21T12:25:35-0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.12 (Eclipse Adoptium)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public OrderPurchaseResponse toPurchaseResponse(Order order, List<OrderItem> orderItems) {
        if ( order == null && orderItems == null ) {
            return null;
        }

        Long id = null;
        BigDecimal totalAmount = null;
        if ( order != null ) {
            id = order.getId();
            totalAmount = order.getTotalAmount();
        }
        List<OrderItemResponse> items = null;
        items = orderItemListToOrderItemResponseList( orderItems );

        OrderPurchaseResponse orderPurchaseResponse = new OrderPurchaseResponse( id, totalAmount, items );

        return orderPurchaseResponse;
    }

    @Override
    public OrderResponse toHistoryResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        Long id = null;
        BigDecimal totalAmount = null;
        OrderStatus status = null;

        id = order.getId();
        totalAmount = order.getTotalAmount();
        status = order.getStatus();

        OrderResponse orderResponse = new OrderResponse( id, totalAmount, status );

        return orderResponse;
    }

    @Override
    public List<OrderResponse> toHistoryResponseList(List<Order> orders) {
        if ( orders == null ) {
            return null;
        }

        List<OrderResponse> list = new ArrayList<OrderResponse>( orders.size() );
        for ( Order order : orders ) {
            list.add( toHistoryResponse( order ) );
        }

        return list;
    }

    protected List<OrderItemResponse> orderItemListToOrderItemResponseList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItemResponse> list1 = new ArrayList<OrderItemResponse>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( orderItemMapper.toOrderItemResponse( orderItem ) );
        }

        return list1;
    }
}
