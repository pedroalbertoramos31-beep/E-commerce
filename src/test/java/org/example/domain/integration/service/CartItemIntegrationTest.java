package org.example.domain.integration.service;

import jakarta.transaction.Transactional;
import org.example.domain.cart.Cart;
import org.example.domain.cart.CartQuery;
import org.example.domain.cart.CartRepository;
import org.example.domain.cart_item.CartItem;
import org.example.domain.cart_item.CartItemQuery;
import org.example.domain.cart_item.CartItemRepository;
import org.example.domain.cart_item.CartItemService;
import org.example.domain.cart_item.dto.request.CartItemUpsertRequest;
import org.example.domain.cart_item.dto.response.CartItemResponse;
import org.example.domain.category.CategoryRepository;
import org.example.domain.fixture.dto.CartItemDTOFixture;
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
    @DisplayName("Upsert Cart Item")
    public class UpsertCartItemTest{

        User user;
        Cart cart;
        Product product;

        @BeforeEach
        public void setUp(){

            this.user = userRepository.saveAndFlush(UserFixture.builder().build());

            this.cart = cartRepository.saveAndFlush(CartFixture.builder().user(this.user).build());

            this.product = productRepository.saveAndFlush(ProductFixture.builder().vendor(this.user).build());


        }

        @Test
        @DisplayName("Success; item is added to cart")
        public void success_ItemAdded(){

            // ARRANGE

            CartItemUpsertRequest request = CartItemDTOFixture.cartItemUpsertRequest(3);

            // ACT

            CartItemResponse response = cartItemService.upsertCartItem(request, product.getId(), this.cart.getId());

            // ASSERT

            assertThat(response.quantity()).isEqualTo(request.quantity());

            CartItem addedItem = cartItemQuery.findByProductIdAndCartId(product.getId(), this.user.getId());

            assertCartItemResponse(response, addedItem);

        }

        @Test
        @DisplayName("Success; cart item quantity is updated")
        public void success_CartItemQuantityUpdated(){

            // ARRANGE

            CartItem existingItem = cartItemRepository.saveAndFlush(CartItemFixture.builder().product(this.product).cart(this.cart).build());

            CartItemUpsertRequest request = CartItemDTOFixture.cartItemUpsertRequest(1);

            // ACT

            CartItemResponse response = cartItemService.upsertCartItem(request, existingItem.getProduct().getId(), existingItem.getCart().getId());

            // ASSERT

            assertThat(response.id()).isEqualTo(existingItem.getId());

            assertThat(response.quantity()).isEqualTo(request.quantity());

            CartItem updatedItem = cartItemQuery.findByProductIdAndCartId(this.product.getId(), this.user.getId());

            assertCartItemResponse(response, updatedItem);

        }

        public void assertCartItemResponse(CartItemResponse response, CartItem item){

            assertThat(response.id()).isEqualTo(item.getId());

            assertThat(response.quantity()).isEqualTo(item.getQuantity());

            assertThat(response.cart().getId()).isEqualTo(item.getCart().getId());

            assertThat(response.product().getId()).isEqualTo(item.getProduct().getId());

        }

    }

    @Nested
    @DisplayName("Delete Cart Item")
    public class DeleteCartItemTest{


        @Test
        @DisplayName("Success; deletes item from cart")
        public void success_DeletesItem(){


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
