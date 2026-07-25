package org.example.domain.order_item;

import org.example.domain.product.ProductMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderItemMapper {

    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

}
