package org.example.domain.cart;

import org.example.domain.cart.dto.response.CartItemsResponse;
import org.example.domain.cart_item.CartItem;
import org.example.domain.cart_item.CartItemMapper;
import org.example.domain.product.ProductMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, CartItemMapper.class})
public interface CartMapper {

    @Mapping(source = "cart.id", target = "id")
    @Mapping(source = "cartItems", target = "items")
    CartItemsResponse toCartItemsResponse(Cart cart, List<CartItem> cartItems);


}
