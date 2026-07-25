package org.example.domain.cart;

import lombok.RequiredArgsConstructor;
import org.example.domain.cart_item.CartItemRepository;
import org.example.infrastructure.exception.error.CartException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartQuery {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;


    public Cart findById(Long userId){
            return cartRepository.findById(userId)
                    .orElseThrow(CartException.CartNotFound::new);

    }

}

