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
import org.example.domain.factory.CartItemTestData;
import org.example.domain.factory.CartTestData;
import org.example.domain.factory.ProductTestData;
import org.example.domain.factory.UserTestData;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
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

            this.user = userRepository.saveAndFlush(UserTestData.simpleUser());

            this.cart = cartRepository.saveAndFlush(CartTestData.simpleCart(this.user));

            this.product = productRepository.saveAndFlush(ProductTestData.simpleProduct(this.user));


        }

        @Test
        @DisplayName("Success; item is added to cart")
        public void success_ItemAdded(){

            // ARRANGE

            CartItemUpsertRequest request = CartItemTestData.cartItemUpsertRequest(3);

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

            CartItem existingItem = cartItemRepository.saveAndFlush(CartItemTestData.simpleCartItem(this.product, this.cart));

            CartItemUpsertRequest request = CartItemTestData.cartItemUpsertRequest(1);

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

            User user = userRepository.saveAndFlush(UserTestData.simpleUser());

            Cart cart = cartRepository.saveAndFlush(CartTestData.simpleCart(user));

            Product product = productRepository.saveAndFlush(ProductTestData.simpleProduct(user));

            CartItem item = cartItemRepository.saveAndFlush(CartItemTestData.simpleCartItem(product, cart));

            // ACT

            cartItemService.deleteProduct(item.getProduct().getId(), user.getId());

            // ASSERT

            Optional<CartItem> deleted = cartItemRepository.findById(item.getId());

            assertThat(deleted).isEmpty();



        }



    }
}
