package org.example.domain.cart_item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.cart.CartQuery;
import org.example.domain.cart.CartRepository;
import org.example.domain.cart_item.dto.request.CartItemQuantityRequest;
import org.example.domain.cart_item.dto.response.CartItemResponse;
import org.example.domain.product.Product;
import org.example.domain.product.ProductQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CartItemService {


    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    private final CartQuery cartQuery;
    private final CartItemQuery cartItemQuery;

    private final ProductQuery productQuery;

    private final CartItemMapper cartItemMapper;


    @Transactional
    public void deleteProduct(Long productId, Long userId){
        cartItemQuery.deleteByProductIdAndCartId(productId, userId);
    }


    @Transactional
    public CartItemResponse upsertCartItem(CartItemQuantityRequest request, Long productId, Long userId) {

        Product product = productQuery.findById(productId);

        List<CartItem> items = cartItemQuery.getByUserId(userId);

        productQuery.verifyAvailableStock(product.getStock(), request.quantity());

        CartItem item = upsertItem(items, request, product, userId);

        return cartItemMapper.toCartItemResponse(item);

    }

    // PRIVATE METHODS

    private CartItem upsertItem(List<CartItem> items, CartItemQuantityRequest request, Product product, Long userId){

        Optional<CartItem> existingCartItem = extractItem(product.getId(), items);

        CartItem item;

        if (existingCartItem.isPresent()){

            item = existingCartItem.get();

            item.updateQuantity(request.quantity());

        } else {

            item = CartItem.create(request.quantity(), product, cartRepository.getReferenceById(userId));

            cartItemRepository.save(item);

            items.add(item);

        }

        return item;

    }

    private Optional<CartItem> extractItem(Long productId, List<CartItem> items) {
        return items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

    }

}
