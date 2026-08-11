package org.example.domain.cart;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.cart.dto.response.CartItemsResponse;
import org.example.domain.cart_item.CartItem;
import org.example.domain.cart_item.CartItemQuery;
import org.example.domain.cart_item.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CartService {


    private final CartQuery cartQuery;
    private final CartItemQuery cartItemQuery;


    private final CartItemRepository cartItemRepository;

    private final CartMapper cartMapper;

    // ******************** METHODS ********************

    @Transactional
    public CartItemsResponse getCartItems(Long userId){

        Cart cart = cartQuery.findById(userId);

        List<CartItem> items = cartItemQuery.getByUserIdWithProduct(userId);

        return cartMapper.toCartItemsResponse(cart, items);
    }







}
