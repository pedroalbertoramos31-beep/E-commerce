package org.example.domain.integration.service;

import jakarta.transaction.Transactional;
import org.example.domain.cart.Cart;
import org.example.domain.cart.CartQuery;
import org.example.domain.cart.CartRepository;
import org.example.domain.cart_item.CartItem;
import org.example.domain.cart_item.CartItemQuery;
import org.example.domain.cart_item.CartItemRepository;
import org.example.domain.fixture.entity.*;
import org.example.domain.order.*;
import org.example.domain.order.dto.response.OrderPurchaseResponse;
import org.example.domain.order.dto.response.OrderResponse;
import org.example.domain.order_item.OrderItem;
import org.example.domain.order_item.OrderItemQuery;
import org.example.domain.order_item.OrderItemResponse;
import org.example.domain.product.Product;
import org.example.domain.product.ProductRepository;
import org.example.domain.product_stats.ProductStatsRepository;
import org.example.domain.user.User;
import org.example.domain.user.UserQuery;
import org.example.domain.user.UserRepository;
import org.example.infrastructure.exception.error.CartException;
import org.example.infrastructure.exception.error.OrderException;
import org.example.infrastructure.exception.error.ProductException;
import org.example.infrastructure.exception.error.UserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "file:.env")
public class OrderIntegrationTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderQuery orderQuery;

    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private ProductStatsRepository productStatsRepository;
    @Autowired private OrderRepository orderRepository;

    @Autowired private CartQuery cartQuery;
    @Autowired private CartItemQuery cartItemQuery;
    @Autowired private UserQuery userQuery;
    @Autowired private OrderItemQuery orderItemQuery;



    @Nested
    @DisplayName("Pay Cart")
    class PlayCartTest{

        private User user;
        private Cart cart;
        private List<CartItem> items = new ArrayList<>();

        @BeforeEach
        void setUp(){

            this.user = userRepository.saveAndFlush(UserFixture.builder().build());

            this.cart = cartRepository.saveAndFlush(CartFixture.builder().user(user).build());

        }

        @Test
        @DisplayName("Success; cart is paid")
        void shouldPayCartAndGenerateOrder_WhenRequestIsValid(){

            // ARRANGE

            setupProduct("Apple", BigDecimal.valueOf(1), 10);

            setupProduct("Banana", BigDecimal.valueOf(1), 7);

            BigDecimal expectedTotal = calculateTotal(this.items);

            BigDecimal expectedBalance = this.user.getBalance().subtract(expectedTotal);

            // ACT

            OrderPurchaseResponse response = orderService.payCart(this.user.getId());

            // ASSERT

            assertOrderPurchaseResponse(response, expectedTotal);

            assertOrderPersistence(response, expectedTotal);

            assertCartItemsAreDeleted();

            assertUserBalanceIsDeducted(expectedBalance);

        }

        @Test
        @DisplayName("Failure; user does not exist")
        void shouldThrowException_WhenUserDoesNotExist(){

            // ARRANGE

            Long notExistingId = -1L;

            // ACT & ASSERT

            assertThatThrownBy(() -> orderService.payCart(notExistingId))
                    .isInstanceOf(UserException.UserNotFound.class);



        }

        @Test
        @DisplayName("Failure; user does not have items to pay for")
        void shouldThrowException_WhenUserDoesNotHaveItems(){

            // ACT & ASSERT

            assertThatThrownBy(() -> orderService.payCart(this.user.getId()))
                    .isInstanceOf(CartException.CartIsEmpty.class);


        }

        @Test
        @DisplayName("Failure; user does not have enough balance to pay")
        void shouldThrowException_WhenUserDoesNotHaveEnoughBalance(){

            // ARRANGE

            setupProduct("Apple", BigDecimal.valueOf(1000), 10);

            // ACT & ASSERT

            assertThatThrownBy(() -> orderService.payCart(this.user.getId()))
                    .isInstanceOf(UserException.InsufficientBalance.class);


        }

        @Test
        @DisplayName("Failure; there is not enough stock to sell")
        void shouldThrowException_WhenStockIsInsufficient(){

            // ARRANGE

            setupProduct("Apple", BigDecimal.valueOf(1), -1);

            // ACT & ASSERT

            assertThatThrownBy(() -> orderService.payCart(this.user.getId()))
                    .isInstanceOf(ProductException.InsufficientStock.class);


        }

        private void assertOrderItemResponse(OrderItemResponse orderItemResponse, CartItem item){

            assertThat(item.getProduct().getId()).isEqualTo(orderItemResponse.product().id());

            assertThat(item.getQuantity()).isEqualTo(orderItemResponse.quantity());

            assertThat(item.getProduct().getPrice()).isEqualTo(orderItemResponse.purchasedAt());

        }

        private void assertOrderItemPersistence(OrderItem orderItem, CartItem item){

            assertThat(item.getProduct().getId()).isEqualTo(orderItem.getProduct().getId());

            assertThat(item.getQuantity()).isEqualTo(orderItem.getQuantity());

            assertThat(item.getProduct().getPrice()).isEqualTo(orderItem.getPurchasedAt());

        }

        private Product setupProduct(String name, BigDecimal price, Integer stock) {

            Product product = ProductFixture.builder().vendor(this.user)
                    .name(name)
                    .price(price)
                    .stock(stock)
                    .build();

            productRepository.saveAndFlush(product);

            productStatsRepository.saveAndFlush(ProductStatsFixture.builder().product(product).build());

            CartItem item = cartItemRepository.saveAndFlush(CartItemFixture.builder().product(product).cart(this.cart).build());

            this.items.add(item);

            return product;
        }

        private BigDecimal calculateTotal(List<CartItem> items){
            return  items.stream()
                    .map(item -> item.getProduct().getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        private void assertOrderPurchaseResponse(OrderPurchaseResponse response, BigDecimal expectedTotal){

            assertThat(response.totalAmount()).isEqualByComparingTo(expectedTotal);

            assertOrderItemResponse(response.items().get(0), this.items.get(0));

            assertOrderItemResponse(response.items().get(1), this.items.get(1));

        }

        private void assertOrderPersistence(OrderPurchaseResponse response, BigDecimal expectedTotal){

            Order order = orderQuery.findByIdAndUserId(response.id(), this.user.getId());

            assertThat(order.getTotalAmount()).isEqualByComparingTo(expectedTotal);

            assertThat(order.getStatus()).isEqualTo(OrderStatus.BOUGHT);

            List<OrderItem> orderItems = orderItemQuery.getByOrderId(response.id());

            assertOrderItemPersistence(orderItems.get(0), this.items.get(0));

            assertOrderItemPersistence(orderItems.get(1), this.items.get(1));

        }

        private void assertCartItemsAreDeleted(){

            List<CartItem> items = cartItemQuery.getByUserId(this.user.getId());

            assertThat(items).isEmpty();

        }

        private void assertUserBalanceIsDeducted(BigDecimal expectedBalance){

            this.user = userQuery.findById(this.user.getId());

            assertThat(expectedBalance).isEqualByComparingTo(this.user.getBalance());

        }
    }

    @Nested
    @DisplayName("Get Orders")
    class GetOrdersTest{

        User user;
        List<Order> orders = new ArrayList<>();

        @BeforeEach
        public void setUp(){

            this.user = userRepository.saveAndFlush(UserFixture.builder().build());

            Order order1 = orderRepository.saveAndFlush(OrderFixture.builder().user(this.user).totalAmount(BigDecimal.valueOf(100)).build());
            Order order2 = orderRepository.saveAndFlush(OrderFixture.builder().user(this.user).totalAmount(BigDecimal.valueOf(50)).build());

            this.orders.add(order1);
            this.orders.add(order2);

        }

        @Test
        @DisplayName("Success; retrieve all user orders")
        public void shouldGetAllUserOrders_WhenRequestIsValid(){

            // ACT

            List<OrderResponse> listResponse = orderService.getOrders(this.user.getId());

            // ASSERT

            assertOrderResponse(listResponse.get(0), this.orders.get(0));

            assertOrderResponse(listResponse.get(1), this.orders.get(1));

        }

        public void assertOrderResponse(OrderResponse response, Order order){

            assertThat(response.id()).isEqualTo(order.getId());

            assertThat(response.totalAmount()).isEqualTo(order.getTotalAmount());

            assertThat(response.status()).isEqualTo(order.getStatus());

        }


    }

    @Nested
    @DisplayName("Find Order")
    class FindOrderTest{

        User user;
        Order order;

        @BeforeEach
        public void setUp() {

            this.user = userRepository.saveAndFlush(UserFixture.builder().build());
            this.order = orderRepository.saveAndFlush(OrderFixture.builder().user(this.user).totalAmount(BigDecimal.valueOf(100)).build());

        }

        @Test
        @DisplayName("Success; order id exist")
        public void shouldRetrieveOrder_WhenRequestIsValid(){

            // ACT

            OrderResponse response = orderService.findOrder(this.order.getId(), this.user.getId());

            // ASSERT

            assertThat(response.id()).isEqualTo(this.order.getId());

            assertThat(response.totalAmount()).isEqualTo(this.order.getTotalAmount());

            assertThat(response.status()).isEqualTo(this.order.getStatus());

        }

        @Test
        @DisplayName("Failure; order id does not exist")
        public void shouldThrowException_WhenOrderIdDoesNotExist(){

            // ARRANGE

            Long nonExistingId = 999L;

            // ACT & ASSERT

            assertThatThrownBy(() -> orderService.findOrder(nonExistingId, this.user.getId()))
                    .isInstanceOf(OrderException.OrderNotFound.class);

        }
    }



}
