package org.example.domain.order;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.cart.CartQuery;
import org.example.domain.cart.CartRepository;
import org.example.domain.cart_item.CartItem;
import org.example.domain.cart_item.CartItemQuery;
import org.example.domain.order.dto.response.OrderPurchaseResponse;
import org.example.domain.order.dto.response.OrderResponse;
import org.example.domain.order_item.OrderItem;
import org.example.domain.order_item.OrderItemRepository;
import org.example.domain.product.Product;
import org.example.domain.product.ProductQuery;
import org.example.domain.product_stats.ProductStats;
import org.example.domain.product_stats.ProductStatsQuery;
import org.example.domain.product_stats.ProductStatsRepository;
import org.example.domain.user.User;
import org.example.domain.user.UserQuery;
import org.example.domain.user.UserRepository;
import org.example.infrastructure.exception.error.CartException;
import org.example.infrastructure.exception.error.UserException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final CartRepository cartRepo;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductStatsRepository productStatsRepository;
    private final UserRepository userRepository;

    private final CartQuery cartQuery;
    private final ProductQuery productQuery;
    private final ProductStatsQuery productStatsQuery;
    private final CartItemQuery cartItemQuery;
    private final UserQuery userQuery;
    private final OrderQuery orderQuery;

    private final OrderMapper orderMapper;


    @Transactional
    public OrderPurchaseResponse payCart(Long userId){

        User user = userQuery.findById(userId);

        List<CartItem> cartItems = getItems(userId);

        Order order = createOrder(userId, user.getBalance(), cartItems);

        List<OrderItem> orderItems = createOrderItem(cartItems, order);

        List<Long> ids = extractIds(cartItems);

        cartItemQuery.deleteByIds(ids);

        updateProductAndStats(orderItems);

        user.subtractBalance(order.getTotalAmount());

        return orderMapper.toPurchaseResponse(order, orderItems);
    }

    @Transactional
    public List<OrderResponse> getOrders(Long userId){

        List<Order> orders = orderQuery.getByUserId(userId);

        return orderMapper.toHistoryResponseList(orders);
    }

    @Transactional
    public OrderResponse findOrder(Long orderId, Long userId){

        Order order = orderQuery.findByIdAndUserId(orderId, userId);

        return orderMapper.toHistoryResponse(order);
    }

    // ==========================================
    // PRIVATE METHODS
    // ==========================================

    // region

    private List<Long> extractIds(List<CartItem> items){
        return items.stream()
                .map(CartItem::getId)
                .toList();
    }

    private List<CartItem> getItems(Long userId){

        List<CartItem> items = cartItemQuery.getByUserId(userId);

        if (items.isEmpty()) {
            throw new CartException.CartIsEmpty();
        }

        return items;
    }

    private Order createOrder(Long userId, BigDecimal balance, List<CartItem> items){

        BigDecimal totalAmount = calculateTotalAmount(items, balance);

        Order order = Order.create(
                totalAmount,
                userRepository.getReferenceById(userId)
        );

        return orderRepository.save(order);

    }

    private BigDecimal calculateTotalAmount(List<CartItem> items, BigDecimal balance){

        BigDecimal amount = items.stream()
                .map(item -> {
                    BigDecimal price = item.getProduct().getPrice();
                    BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
                    return price.multiply(quantity);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (balance.compareTo(amount) < 0){
            throw new UserException.InsufficientBalance(amount, balance);
        }

        return amount;
    }

    private List<OrderItem> createOrderItem(List<CartItem> items, Order order) {

        List<OrderItem> orderItemsToSave = new ArrayList<>();

        for (CartItem item : items) {

            Product product = item.getProduct();

            productQuery.verifyAvailableStock(product.getStock(), item.getQuantity());

            OrderItem orderItem = OrderItem.create(
                    item.getProduct(),
                    item.getQuantity(),
                    item.getProduct().getPrice(),
                    order
            );

            orderItemsToSave.add(orderItem);
        }

        return orderItemRepository.saveAll(orderItemsToSave);

    }

    private void updateProductAndStats(List<OrderItem> orderItems){

        List<Long> productIds = orderItems.stream()
                .map(item -> item.getProduct().getId())
                .toList();

        List<ProductStats> productStats = productStatsQuery.getByProductIds(productIds);

        Map<Long, ProductStats> statsMap = productStats.stream()
                .collect(Collectors.toMap(
                        stat -> stat.getProduct().getId(),
                        stat -> stat));

        for (OrderItem orderItem : orderItems){

            Product product = orderItem.getProduct();

            ProductStats stats = statsMap.get(product.getId());

            product.decreaseStock(orderItem.getQuantity());

            stats.updateSalesCount(orderItem.getQuantity());

        }
    }

    // endregion
}
