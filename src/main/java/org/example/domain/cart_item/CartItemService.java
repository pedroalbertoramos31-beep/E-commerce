package org.example.domain.cart_item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.cart.CartQuery;
import org.example.domain.cart.CartRepository;
import org.example.domain.cart_item.dto.request.CartItemQuantityRequest;
import org.example.domain.cart_item.dto.response.CartItemResponse;
import org.example.domain.product.Product;
import org.example.domain.product.ProductQuery;
import org.example.domain.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CartItemService {


    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    private final CartQuery cartQuery;
    private final CartItemQuery cartItemQuery;

    private final ProductQuery productQuery;

    private final CartItemMapper cartItemMapper;


    @Transactional
    public void deleteProduct(Long productId, Long userId){
        cartItemQuery.deleteByProductIdAndCartId(productId, userId);
    }

    @Transactional
    public CartItemResponse addToCart(CartItemQuantityRequest request, Long productId, Long userId) {

        Product product = productQuery.findById(productId);

        List<CartItem> items = cartItemQuery.getByUserIdWithProduct(userId);

        Optional<CartItem> optionalCartItem = extractItem(product.getId(), items);

        CartItem item = upsertItem(items, request.quantity(), product, userId, optionalCartItem);

        return cartItemMapper.toCartItemResponse(item);

    }

    @Transactional
    public CartItemResponse updateItemQuantity(CartItemQuantityRequest request, Long productId, Long userId){

        Product product = productQuery.findById(productId);

        List<CartItem> items = cartItemQuery.getByUserIdWithProduct(userId);

        Optional<CartItem> optionalCartItem = extractItem(product.getId(), items);

        CartItem item = changeQuantity(optionalCartItem, request.quantity(), product);

        return cartItemMapper.toCartItemResponse(item);

    }

    // PRIVATE METHODS

    private CartItem changeQuantity(Optional<CartItem> optionalCartItem, Integer quantity, Product product){

        CartItem item = cartItemQuery.verifyIsPresent(optionalCartItem);

        productQuery.verifyAvailableStock(product.getStock(), quantity);

        item.changeQuantity(quantity);

        return item;

    }

    private CartItem upsertItem(List<CartItem> items, Integer quantity, Product product, Long userId, Optional<CartItem> optionalCartItem){

        CartItem item;

        if (optionalCartItem.isPresent()){

            item = optionalCartItem.get();

            Integer totalQuantity = item.getQuantity() + quantity;

            productQuery.verifyAvailableStock(product.getStock(), totalQuantity);

            item.addQuantity(quantity);

        } else {

            productQuery.verifyAvailableStock(product.getStock(), quantity);

            item = CartItem.create(
                    quantity,
                    productRepository.getReferenceById(product.getId()),
                    cartRepository.getReferenceById(userId));

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
