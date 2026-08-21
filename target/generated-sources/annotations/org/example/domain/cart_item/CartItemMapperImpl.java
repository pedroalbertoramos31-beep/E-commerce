package org.example.domain.cart_item;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.domain.cart_item.dto.response.CartItemResponse;
import org.example.domain.product.Product;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-21T12:25:35-0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.12 (Eclipse Adoptium)"
)
@Component
public class CartItemMapperImpl implements CartItemMapper {

    @Override
    public CartItemResponse toCartItemResponse(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }

        Long productId = null;
        Long id = null;
        Integer quantity = null;

        productId = cartItemProductId( cartItem );
        id = cartItem.getId();
        quantity = cartItem.getQuantity();

        CartItemResponse cartItemResponse = new CartItemResponse( id, productId, quantity );

        return cartItemResponse;
    }

    @Override
    public List<CartItemResponse> toCartItemListResponse(List<CartItem> cartItems) {
        if ( cartItems == null ) {
            return null;
        }

        List<CartItemResponse> list = new ArrayList<CartItemResponse>( cartItems.size() );
        for ( CartItem cartItem : cartItems ) {
            list.add( toCartItemResponse( cartItem ) );
        }

        return list;
    }

    private Long cartItemProductId(CartItem cartItem) {
        Product product = cartItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }
}
