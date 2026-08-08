package org.example.domain.integration.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.example.domain.cart.Cart;
import org.example.domain.cart.CartQuery;
import org.example.domain.cart.CartRepository;
import org.example.domain.cart.CartService;
import org.example.domain.cart.dto.response.CartItemsResponse;
import org.example.domain.cart_item.CartItem;
import org.example.domain.cart_item.CartItemRepository;
import org.example.domain.cart_item.dto.response.CartItemResponse;
import org.example.domain.fixture.entity.CartFixture;
import org.example.domain.fixture.entity.CartItemFixture;
import org.example.domain.fixture.entity.ProductFixture;
import org.example.domain.fixture.entity.UserFixture;
import org.example.domain.product.Product;
import org.example.domain.product.ProductQuery;
import org.example.domain.product.ProductRepository;
import org.example.domain.user.User;
import org.example.domain.user.UserQuery;
import org.example.domain.user.UserRepository;
import org.example.domain.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "file:.env")
public class CartIntegrationTest {

    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;

    @Autowired private CartQuery cartQuery;
    @Autowired private ProductQuery productQuery;
    @Autowired private UserQuery userQuery;

    @Autowired private UserService userService;
    @Autowired private CartService cartService;

    @Autowired private EntityManager entityManager;

    @Nested
    @DisplayName("Get cart items")
    class GetCartItems {

        private User user;
        private Cart cart;
        private Product product;
        private List<CartItem> items = new ArrayList<>();

        @BeforeEach
        void setUp(){

            this.user = userRepository.saveAndFlush(UserFixture.builder().build());
            this.cart = cartRepository.saveAndFlush(CartFixture.builder().user(user).build());
            this.product = productRepository.saveAndFlush(ProductFixture.builder().vendor(user).build());
            this.items.add(cartItemRepository.saveAndFlush(CartItemFixture.builder().product(product).cart(cart).build()));

        }

        @Test
        @DisplayName("Success; get cart items")
        public void shouldGetCartItems_WhenRequestIsValid() {

            // ACT

            CartItemsResponse response = cartService.getCartItems(this.user.getId());

            // ASSERT - RESPONSE

            CartItemResponse itemResponse = response.items().getFirst();
            CartItem item = this.items.getFirst();

            assertThat(response.id()).isEqualTo(this.user.getId());

            assertThat(itemResponse.id()).isEqualTo(item.getId());

            assertThat(itemResponse.quantity()).isEqualTo(item.getQuantity());

            assertThat(itemResponse.productId()).isEqualTo(item.getProduct().getId());

        }
    }

}
