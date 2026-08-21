package org.example.domain.order;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.example.domain.order.dto.response.OrderPurchaseResponse;
import org.example.domain.order.dto.response.OrderResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderPurchaseResponse> payCart(@AuthenticationPrincipal(expression = "id") Long userId) {

        OrderPurchaseResponse order = orderService.payCart(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(@AuthenticationPrincipal(expression = "id") Long userId) {

        List<OrderResponse> order = orderService.getOrders(userId);

        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findOrder(
            @Positive @PathVariable Long orderId,
            @AuthenticationPrincipal(expression = "id") Long userId) {

        OrderResponse order = orderService.findOrder(orderId, userId);

        return ResponseEntity.status(HttpStatus.OK).body(order);
    }


}
