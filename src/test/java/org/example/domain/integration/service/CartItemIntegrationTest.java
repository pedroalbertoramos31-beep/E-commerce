package org.example.domain.integration.service;

import jakarta.transaction.Transactional;
import org.example.domain.cart.Cart;
import org.example.domain.cart.CartQuery;
import org.example.domain.cart.CartRepository;
import org.example.domain.cart_item.CartItem;
import org.example.domain.cart_item.CartItemQuery;
import org.example.domain.cart_item.CartItemRepository;
import org.example.domain.cart_item.CartItemService;
import org.example.domain.cart_item.dto.request.CartItemQuantityRequest;
import org.example.domain.cart_item.dto.response.CartItemResponse;
import org.example.domain.category.CategoryRepository;
import org.example.domain.fixture.entity.CartFixture;
import org.example.domain.fixture.entity.CartItemFixture;
import org.example.domain.fixture.entity.ProductFixture;
import org.example.domain.fixture.entity.UserFixture;
import org.example.domain.product.Product;
import org.example.domain.product.ProductRepository;
import org.example.domain.product_category.ProductCategoryRepository;
import org.example.domain.user.User;
import org.example.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.domain.assertion.service.CartItemTestAssertion.assertCartItemPersistence;
import static org.example.domain.assertion.service.CartItemTestAssertion.assertCartItemResponse;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "file:.env")
public class CartItemIntegrationTest {

    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ProductCategoryRepository productCategoryRepository;
    @Autowired private CartItemQuery cartItemQuery;

    @Autowired private CartItemService cartItemService;

    @Autowired private CartQuery cartQuery;


    @Nested
    @DisplayName("Add Product To Cart")
    public class AddProductToCart{

        User user;
        Product product;
        Cart cart;

        @BeforeEach
        public void setUp() {

            this.user = userRepository.save(UserFixture.builder().build());

            this.cart = cartRepository.save(CartFixture.builder().user(user).build());

            this.product = productRepository.save(ProductFixture.builder().vendor(user).build());

        }

        @Test
        @DisplayName("Success; add product to cart")
        public void success_ProductIsAddedToCart(){

            // ARRANGE

            CartItemQuantityRequest request = new CartItemQuantityRequest(5);

            CartItemResponse expectedResponse = new CartItemResponse(
                    -1L,
                    product.getId(),
                    request.quantity()
            );

            CartItem expectedItemPersisted = CartItemFixture.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.quantity())
                    .build();

            // ACT

            CartItemResponse response = cartItemService.addToCart(request, product.getId(), user.getId());

            // ASSERT - RESPONSE

            assertCartItemResponse(expectedResponse, response);

            // ASSERT - PERSISTENCE

            CartItem item = cartItemQuery.findByProductIdAndCartId(product.getId(), cart.getId());

            assertCartItemPersistence(expectedItemPersisted, item);

        }

        @Test
        @DisplayName("Success; increase item quantity of an item in cart")
        public void success_ItemQuantityInCartIsIncreased(){

            // ARRANGE

            CartItemQuantityRequest request = new CartItemQuantityRequest(5);

            CartItem existingItem = cartItemRepository.saveAndFlush(CartItemFixture.builder()
                            .cart(this.cart)
                            .product(this.product)
                            .quantity(5)
                            .build());

            CartItemResponse expectedResponse = new CartItemResponse(
                    -1L,
                    product.getId(),
                    5 + request.quantity()
            );

            CartItem expectedItemPersisted = CartItemFixture.builder()
                    .cart(this.cart)
                    .product(this.product)
                    .quantity(5 + request.quantity())
                    .build();

            // ACT

            CartItemResponse response = cartItemService.addToCart(request, this.product.getId(), this.cart.getId());

            // ASSERT - RESPONSE

            assertCartItemResponse(expectedResponse, response);

            // ASSERT - PERSISTENCE

            CartItem item = cartItemQuery.findByProductIdAndCartId(product.getId(), cart.getId());

            assertCartItemPersistence(expectedItemPersisted, item);

        }


    }

    @Nested
    @DisplayName("Change Item Quantity")
    public class ChangeItemQuantity{


        @Test
        @DisplayName("Success: item quantity is changed")
        public void success_ItemQuantityIsChanged(){

            // ARRANGE

            User user = userRepository.save(UserFixture.builder().build());

            Cart cart = cartRepository.save(CartFixture.builder().user(user).build());

            Product product = productRepository.save(ProductFixture.builder().vendor(user).build());

            CartItem existingItem = cartItemRepository.save(CartItemFixture.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(1)
                    .build());

            CartItemQuantityRequest request = new CartItemQuantityRequest(5);

            CartItemResponse expectedResponse = new CartItemResponse(
                    -1L,
                    product.getId(),
                    5
            );

            CartItem expectedItemPersisted = CartItemFixture.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(5)
                    .build();

            // ACT

            CartItemResponse response = cartItemService.updateItemQuantity(request, product.getId(), user.getId());

            // ASSERT - RESPONSE

            assertCartItemResponse(expectedResponse, response);

            // ASSERT - PERSISTENCE

            CartItem item = cartItemQuery.findByProductIdAndCartId(product.getId(), cart.getId());

            assertCartItemPersistence(expectedItemPersisted, item);

        }

    }

    @Nested
    @DisplayName("Delete Cart Item")
    public class DeleteCartItemTest {


        @Test
        @DisplayName("Success; deletes item from cart")
        public void success_DeletesItem() {


            // ARRANGE

            User user = userRepository.saveAndFlush(UserFixture.builder().build());

            Cart cart = cartRepository.saveAndFlush(CartFixture.builder().user(user).build());

            Product product = productRepository.saveAndFlush(ProductFixture.builder().vendor(user).build());

            CartItem item = cartItemRepository.saveAndFlush(CartItemFixture.builder().product(product).cart(cart).build());

            // ACT

            cartItemService.deleteProduct(item.getProduct().getId(), user.getId());

            // ASSERT

            Optional<CartItem> deleted = cartItemRepository.findById(item.getId());

            assertThat(deleted).isEmpty();


        }
    }
}

