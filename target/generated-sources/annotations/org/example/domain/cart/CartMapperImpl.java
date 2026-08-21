package org.example.domain.cart;

import java.util.List;
import javax.annotation.processing.Generated;
import org.example.domain.cart.dto.response.CartItemsResponse;
import org.example.domain.cart_item.CartItem;
import org.example.domain.cart_item.CartItemMapper;
import org.example.domain.cart_item.dto.response.CartItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-21T12:25:35-0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.12 (Eclipse Adoptium)"
)
@Component
public class CartMapperImpl implements CartMapper {

    @Autowired
    private CartItemMapper cartItemMapper;

    @Override
    public CartItemsResponse toCartItemsResponse(Cart cart, List<CartItem> cartItems) {
        if ( cart == null && cartItems == null ) {
            return null;
        }

        Long id = null;
        if ( cart != null ) {
            id = cart.getId();
        }
        List<CartItemResponse> items = null;
        items = cartItemMapper.toCartItemListResponse( cartItems );

        CartItemsResponse cartItemsResponse = new CartItemsResponse( id, items );

        return cartItemsResponse;
    }
}
