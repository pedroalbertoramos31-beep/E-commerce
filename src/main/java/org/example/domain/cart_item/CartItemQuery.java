package org.example.domain.cart_item;

import lombok.RequiredArgsConstructor;
import org.example.infrastructure.exception.error.CartException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemQuery {

    private final CartItemRepository cartItemRepository;

    public CartItem findByProductIdAndCartId(Long productId, Long cartId){
        return cartItemRepository.findByProductIdAndCartId(productId, cartId)
                .orElseThrow(() -> new CartException.ItemNotFound(productId));
    }

    public List<CartItem> getByUserId(Long userId){
        return cartItemRepository.getAllCartItems(userId);
    }

    public void deleteByProductIdAndCartId(Long productId, Long cartId){
        cartItemRepository.deleteItem(productId, cartId);
    }

    public void deleteByIds(List<Long> ids){
        cartItemRepository.deleteItems(ids);
    }


}
