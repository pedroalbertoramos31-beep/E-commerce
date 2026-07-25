package org.example.domain.cart_item;

import org.example.domain.cart_item.dto.response.CartItemResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    CartItemResponse toCartItemResponse(CartItem cartItem);

    List<CartItemResponse> toCartItemListResponse(List<CartItem> cartItems);

}
