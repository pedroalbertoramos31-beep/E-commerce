package org.example.domain.cart_item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.example.domain.cart_item.dto.request.CartItemQuantityRequest;
import org.example.domain.cart_item.dto.response.CartItemResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping("/{productId}")
    public ResponseEntity<CartItemResponse> addToCart(

            @Valid @RequestBody CartItemQuantityRequest request,
            @PathVariable @Positive Long productId,
            @AuthenticationPrincipal(expression = "id") Long userId
    ){

        CartItemResponse response = cartItemService.addToCart(request, productId, userId);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PatchMapping("/{productId}")
    public ResponseEntity<CartItemResponse> updateItemQuantity(

            @Valid @RequestBody CartItemQuantityRequest request,
            @PathVariable @Positive Long productId,
            @AuthenticationPrincipal(expression = "id") Long userId
    ){

        CartItemResponse response = cartItemService.updateItemQuantity(request, productId, userId);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @DeleteMapping("/item/{productId}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable @Positive Long productId,
            @AuthenticationPrincipal(expression = "id") Long userId){

        cartItemService.deleteProduct(userId, productId);

        return ResponseEntity.noContent().build();
    }

}
