package org.example.domain.cart;


import lombok.RequiredArgsConstructor;
import org.example.domain.cart.dto.response.CartItemsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartItemsResponse> getCartItems(@AuthenticationPrincipal(expression = "id") Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getCartItems(userId));
    }
    


}
