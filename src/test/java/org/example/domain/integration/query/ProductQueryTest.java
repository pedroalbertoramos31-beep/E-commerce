package org.example.domain.integration.query;

import jakarta.transaction.Transactional;
import org.example.domain.factory.ProductTestData;
import org.example.domain.factory.UserTestData;
import org.example.domain.product.Product;
import org.example.domain.product.ProductQuery;
import org.example.domain.product.ProductRepository;
import org.example.domain.user.User;
import org.example.domain.user.UserRepository;
import org.example.infrastructure.exception.error.ProductException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "file:.env")
public class ProductQueryTest {

    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;


    @Autowired private ProductQuery productQuery;




    @Nested
    @DisplayName("Find Product By Id")
    public class FindProductByIdTest{

        User user;
        Product product;

        @BeforeEach
        public void setUp(){

            this.user = userRepository.saveAndFlush(UserTestData.simpleUser());

            this.product = productRepository.saveAndFlush(ProductTestData.simpleProduct(this.user));

        }

        @Test
        @DisplayName("Success; retrieve product when exist")
        public void shouldRetrieveProduct_WhenProductExist(){

            // ACT

            Product found = productQuery.findById(this.product.getId());

            // ASSERT

            assertThat(found.getId()).isEqualTo(this.product.getId());

        }

        @Test
        @DisplayName("Failure; fail when product does not exist")
        public void shouldThrowException_WhenProductDoesNotExist(){

            // ARRANGE

            Long nonExistingProductId = 999L;

            // ACT & ASSERT

            assertThatThrownBy(() -> productQuery.findById(nonExistingProductId))
                    .isInstanceOf(ProductException.NotFound.class);

        }


    }

    @Nested
    @DisplayName("Verify Product Owner")
    public class VerifyProductOwner{

        User user;
        Product product;

        @BeforeEach
        public void setUp(){

            this.user = userRepository.saveAndFlush(UserTestData.simpleUser());

            this.product = productRepository.saveAndFlush(ProductTestData.simpleProduct(this.user));

        }

        @Test
        @DisplayName("Success; user is the owner of the product")
        public void shouldPassVerification_WhenUserIsTheProductOwner(){

            // ACT & ASSERT

            assertThatNoException().isThrownBy(() ->
                    productQuery.verifyUserIsOwner(this.product.getVendor().getId(), this.user.getId())
            );
        }


        @Test
        @DisplayName("Failure; user is not the owner of the product")
        public void shouldThrowException_WhenUserIsNotTheProductOwner(){

            // ARRANGE

            Long nonExistingUserId = 999L;

            // ACT & ASSERT

            assertThatThrownBy(() -> productQuery.verifyUserIsOwner(this.user.getId(), nonExistingUserId))
                    .isInstanceOf(ProductException.IllegalResourceAccess.class);

        }

    }

    @Nested
    @DisplayName("Verify Available Stock")
    public class VerifyAvailableStock{

        User user;

        @BeforeEach
        public void setUp(){

            this.user = userRepository.saveAndFlush(UserTestData.simpleUser());

        }

        @Test
        @DisplayName("Success; there is enough stock available")
        public void success_AvailableStock(){

            // ARRANGE

            Product product = productRepository.saveAndFlush(ProductTestData.customProduct(user).stock(1).build());

            Integer enoughQuantity = 1;

            // ACT & ASSERT

            assertThatNoException().isThrownBy(() ->
                    productQuery.verifyAvailableStock(product.getStock(), enoughQuantity));


        }

        @Test
        @DisplayName("Success; there is not enough stock available")
        public void failure_NotEnoughStock(){

            // ARRANGE

            Product product = productRepository.saveAndFlush(ProductTestData.customProduct(user).stock(1).build());

            Integer excidingQuantity = 999;

            // ACT & ASSERT

            assertThatThrownBy(() -> productQuery.verifyAvailableStock(product.getStock(), excidingQuantity))
                    .isInstanceOf(ProductException.InsufficientStock.class);

        }


    }


}
