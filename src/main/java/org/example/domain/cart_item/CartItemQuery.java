package org.example.domain.cart_item;

import lombok.RequiredArgsConstructor;
import org.example.infrastructure.exception.error.CartException;
import org.example.infrastructure.exception.error.CartItemException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemQuery {

    private final CartItemRepository cartItemRepository;

    public CartItem findByProductIdAndCartId(Long productId, Long cartId){
        return cartItemRepository.findByProductIdAndCartId(productId, cartId)
                .orElseThrow(() -> new CartException.ItemNotFound(productId));
    }

    public CartItem verifyIsPresent(Optional<CartItem> optionalCartItem){

        if (optionalCartItem.isEmpty()){
            throw new CartItemException.ItemNotFound();
        }

        return optionalCartItem.get();

    }

    public List<CartItem> getByUserIdWithProduct(Long userId){
        return cartItemRepository.getByUserIdWithProduct(userId);
    }

    public List<CartItem> getByUserId(Long userId){
        return cartItemRepository.getByUserId(userId);
    }

    public void deleteByProductIdAndCartId(Long productId, Long cartId){
        cartItemRepository.deleteItem(productId, cartId);
    }

    public void deleteByIds(List<Long> ids){
        cartItemRepository.deleteItems(ids);
    }


}
