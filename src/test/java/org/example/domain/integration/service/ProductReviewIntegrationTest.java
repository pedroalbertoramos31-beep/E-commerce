package org.example.domain.integration.service;

import jakarta.transaction.Transactional;
import org.example.domain.cart.CartRepository;
import org.example.domain.cart_item.CartItemRepository;
import org.example.domain.category.CategoryRepository;
import org.example.domain.factory.*;
import org.example.domain.order.Order;
import org.example.domain.order.OrderRepository;
import org.example.domain.order_item.OrderItemRepository;
import org.example.domain.product.Product;
import org.example.domain.product.ProductQuery;
import org.example.domain.product.ProductRepository;
import org.example.domain.product_category.ProductCategoryRepository;
import org.example.domain.product_review.ProductReview;
import org.example.domain.product_review.ProductReviewQuery;
import org.example.domain.product_review.ProductReviewRepository;
import org.example.domain.product_review.ProductReviewService;
import org.example.domain.product_review.dto.request.ProductReviewCreationRequest;
import org.example.domain.product_review.dto.response.ProductReviewResponse;
import org.example.domain.product_stats.ProductStats;
import org.example.domain.product_stats.ProductStatsQuery;
import org.example.domain.product_stats.ProductStatsRepository;
import org.example.domain.user.User;
import org.example.domain.user.UserRepository;
import org.example.infrastructure.exception.error.ReviewException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "file:.env")
public class ProductReviewIntegrationTest {

    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private ProductCategoryRepository productCategoryRepository;
    @Autowired private ProductStatsRepository productStatsRepository;
    @Autowired private ProductReviewRepository productReviewRepository;


    @Autowired private ProductReviewService productReviewService;

    @Autowired private ProductQuery productQuery;
    @Autowired private ProductStatsQuery productStatsQuery;
    @Autowired private ProductReviewQuery productReviewQuery;



    @Nested
    @DisplayName("Create Review")
    public class CreateReviewTest{


        User productOwner;
        Product product;
        ProductStats productStats;

        @BeforeEach
        public void setUp(){

            this.productOwner = userRepository.saveAndFlush(UserTestData.simpleUser());

            this.product = productRepository.saveAndFlush(ProductTestData.simpleProduct(this.productOwner));

            this.productStats = productStatsRepository.saveAndFlush(ProductStatsTestData.simpleProductStats(this.product));

        }




        @Test
        @DisplayName("Success; create review")
        public void success_CreateReview(){

            // ARRANGE

            createInitialReview();

            User customer = userRepository.saveAndFlush(UserTestData.simpleUser("Patrick Jane"));

            Order order = orderRepository.saveAndFlush(OrderTestData.simpleOrder(customer, BigDecimal.valueOf(10)));

            orderItemRepository.saveAndFlush(OrderItemTestData.simpleOrderItem(this.product, order, order.getTotalAmount()));

            ProductReviewCreationRequest request = ProductReviewTestData.createReviewRequest(3, "Foo");

            // ACT

            ProductReviewResponse response = productReviewService.createReview(this.product.getId(), customer.getId(), request);

            // ASSERT - Response

            assertThat(response.rating()).isEqualTo(request.rating());

            assertThat(response.comment()).isEqualTo(request.comment());

            // ASSERT - Persistence

            ProductReview review = productReviewQuery.findByIdWithProductAndUser(response.id());

            assertReviewPersistence(response, review);

            // ASSERT - Stats Updated

            ProductStats updatedStats = productStatsQuery.findById(this.product.getId());

            assertThat(updatedStats.getAverageRating()).isEqualTo(4.0);

            assertThat(updatedStats.getReviewCount()).isEqualTo(2);

        }

        @Test
        @DisplayName("Failure; user is the product owner")
        public void failure_UserIsProductOwner(){

            // ARRANGE

            ProductReviewCreationRequest request = ProductReviewTestData.createReviewRequest(3, "Foo");

            // ACT & ASSERT

            assertThatThrownBy(() -> productReviewService.createReview(this.product.getId(), this.productOwner.getId(), request))
                    .isInstanceOf(ReviewException.SelfReview.class);


        }

        @Test
        @DisplayName("Failure; user has not purchased the product")
        public void failure_UserHasNotPurchasedTheProduct(){

            // ARRANGE

            User customer = userRepository.saveAndFlush(UserTestData.simpleUser("JoJo"));

            ProductReviewCreationRequest request = ProductReviewTestData.createReviewRequest(3, "Foo");

            // ACT & ASSERT

            assertThatThrownBy(() -> productReviewService.createReview(this.product.getId(), customer.getId(), request))
                    .isInstanceOf(ReviewException.ProductNotBought.class);


        }

        @Test
        @DisplayName("Failure; user has already reviewed the product")
        public void failure_UserAlreadyReviewedTheProduct(){

            // ARRANGE

            User customer = userRepository.saveAndFlush(UserTestData.simpleUser("Patrick Jane"));

            Order order = orderRepository.saveAndFlush(OrderTestData.simpleOrder(customer, BigDecimal.valueOf(10)));

            orderItemRepository.saveAndFlush(OrderItemTestData.simpleOrderItem(this.product, order, order.getTotalAmount()));

            ProductReviewCreationRequest request = ProductReviewTestData.createReviewRequest(3, "Foo");

            productReviewService.createReview(this.product.getId(), customer.getId(), request);

            // ACT & ASSERT

            assertThatThrownBy(() -> productReviewService.createReview(this.product.getId(), customer.getId(), request))
                    .isInstanceOf(ReviewException.AlreadyReviewed.class);


        }


        public void assertReviewPersistence(ProductReviewResponse response, ProductReview review){

            assertThat(response.id()).isEqualTo(review.getId());

            assertThat(response.rating()).isEqualTo(review.getRating());

            assertThat(response.comment()).isEqualTo(review.getComment());

            assertThat(response.productId()).isEqualTo(review.getProduct().getId());

            assertThat(response.user().getId()).isEqualTo(review.getUser().getId());

        }

        public void createInitialReview(){

            User someoneElse = userRepository.saveAndFlush(UserTestData.simpleUser("Someone Else"));

            Order order = orderRepository.saveAndFlush(OrderTestData.simpleOrder(someoneElse, BigDecimal.valueOf(10)));

            orderItemRepository.saveAndFlush(OrderItemTestData.simpleOrderItem(this.product, order, order.getTotalAmount()));

            ProductReviewCreationRequest request = ProductReviewTestData.createReviewRequest(5, "FooBar");

            productReviewService.createReview(this.product.getId(), someoneElse.getId(), request);

        }

    }

    @Nested
    @DisplayName("Get Product Reviews")
    public class GetProductReviewTest{

        User productOwner;
        Product product;

        @BeforeEach
        public void setUp(){

            this.productOwner = userRepository.saveAndFlush(UserTestData.simpleUser("Patrick Jane"));

            this.product = productRepository.saveAndFlush(ProductTestData.simpleProduct(this.productOwner));

        }

        @Test
        @DisplayName("Success; retrieve all reviews")
        public void success_RetrieveAllReviews(){

            // ARRANGE

            createReview("Mordecai", 2, "FooBar");

            createReview("John Doe", 5, "Foo");

            createReview("Schofield", 3, "Bar");

            Pageable page = PageRequest.of(0, 3);


            // ACT

            Page<ProductReviewResponse> response = productReviewService.getProductReviews(this.product.getId(), page);

            // ASSERT

            assertThat(response.getContent())
                    .extracting(content -> content.user().getUsername())
                    .containsExactlyInAnyOrder("Mordecai", "John Doe", "Schofield");

            assertPaginationContent(response);

        }

        private void assertPaginationContent(Page<ProductReviewResponse> response){

            assertThat(response.getContent()).hasSize(3);

            assertThat(response.getTotalElements()).isEqualTo(3);

            assertThat(response.getTotalPages()).isEqualTo(1);

            assertThat(response.getNumber()).isEqualTo(0);

        }

        private void createReview(String username, Integer rating, String comment){

            User customer = userRepository.saveAndFlush(UserTestData.simpleUser(username));

            ProductReview review = productReviewRepository.saveAndFlush(ProductReviewTestData.simpleProductReview(rating, comment, this.product, customer));

        }


    }

}
