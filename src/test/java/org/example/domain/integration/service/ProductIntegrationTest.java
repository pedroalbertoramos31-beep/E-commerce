package org.example.domain.integration.service;

import jakarta.transaction.Transactional;
import org.example.domain.category.Category;
import org.example.domain.category.CategoryQuery;
import org.example.domain.category.CategoryRepository;
import org.example.domain.category.CategoryStatus;
import org.example.domain.category.dto.response.CategoryResponse;
import org.example.domain.fixture.dto.ProductDTOFixture;
import org.example.domain.fixture.entity.*;
import org.example.domain.product.*;
import org.example.domain.product.dto.request.ProductRegisterRequest;
import org.example.domain.product.dto.request.ProductStockIncreaseRequest;
import org.example.domain.product.dto.response.ProductCardResponse;
import org.example.domain.product.dto.response.ProductFoundResponse;
import org.example.domain.product.dto.response.ProductStatusResponse;
import org.example.domain.product.dto.response.ProductStockResponse;
import org.example.domain.product_category.ProductCategory;
import org.example.domain.product_category.ProductCategoryRepository;
import org.example.domain.product_stats.ProductStats;
import org.example.domain.product_stats.ProductStatsQuery;
import org.example.domain.product_stats.ProductStatsRepository;
import org.example.domain.user.User;
import org.example.domain.user.UserRepository;
import org.example.infrastructure.exception.error.CategoryException;
import org.example.infrastructure.exception.error.ProductException;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.example.domain.assertion.service.CategoryTestAssertion.assertCategoryResponse;
import static org.example.domain.assertion.service.ProductStatsTestAssertion.assertProductStatsResponse;
import static org.example.domain.assertion.service.ProductTestAssertion.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "file:.env")
public class ProductIntegrationTest {

    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductStatsRepository productStatsRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ProductCategoryRepository productCategoryRepository;

    @Autowired private ProductQuery productQuery;
    @Autowired private ProductStatsQuery productStatsQuery;
    @Autowired private CategoryQuery categoryQuery;

    @Autowired private ProductService productService;

    @Nested
    @DisplayName("Find Product Test")
    public class FindProductTest{

        Product product;
        ProductStats productStats;
        List<Category> categories = new ArrayList<>();
        List<ProductCategory> productCategories = new ArrayList<>();
        User user;

        @BeforeEach
        public void setUp(){

            this.user = userRepository.saveAndFlush(UserFixture.builder().build());

            this.product = productRepository.saveAndFlush(ProductFixture.builder().vendor(user).build());
            this.productStats = productStatsRepository.saveAndFlush(ProductStatsFixture.builder().product(product).build());

            this.categories.add(categoryRepository.saveAndFlush(CategoryFixture.builder().build()));
            this.categories.add(categoryRepository.saveAndFlush(CategoryFixture.builder().name("Fruit").status(CategoryStatus.ACTIVE).build()));

            this.productCategories.add(productCategoryRepository.saveAndFlush(ProductCategoryFixture.builder().product(this.product).category(this.categories.get(0)).build()));
            this.productCategories.add(productCategoryRepository.saveAndFlush(ProductCategoryFixture.builder().product(this.product).category(this.categories.get(1)).build()));

        }

        @Test
        @DisplayName("Success; retrieve product")
        public void shouldRetrieveProduct_WhenRequestIsValid(){

            // ACT

            ProductFoundResponse response = productService.findProduct(this.product.getId());

            // ASSERT

            assertProductFoundResponse(response, this.product);

            assertProductStatsResponse(response.stats(), this.productStats);

            assertCategoryResponse(response.categories().get(0), this.productCategories.get(0).getCategory());

            assertCategoryResponse(response.categories().get(1), this.productCategories.get(1).getCategory());

        }

        @Test
        @DisplayName("Failure; when product id doesn't exist")
        public void shouldThrowException_WhenProductIdDoesNotExist(){

            // ARRANGE

            Long nonExistingId = 999L;

            // ACT & ARRANGE

            assertThatThrownBy(() -> productService.findProduct(nonExistingId))
                    .isInstanceOf(ProductException.NotFound.class);

        }




    }

    @Nested
    @DisplayName("Get Product Like Name")
    public class GetProductLikeNameTest{

        User user;
        List<Product> products = new ArrayList<>();
        List<ProductStats> productStats = new ArrayList<>();

        Pageable page = PageRequest.of(0, 10);


        @BeforeEach
        public void setUp(){

            this.user = userRepository.saveAndFlush(UserFixture.builder().build());

            setProducts("Apple");

            setProducts("Apricot");

            setProducts("Aprium");

            setProducts("Banana");

            setProducts("Onion");

        }

        public void setProducts(String name){

            Product product = productRepository.saveAndFlush(ProductFixture.builder().vendor(this.user).name(name).build());

            ProductStats stats = productStatsRepository.saveAndFlush(ProductStatsFixture.builder().product(product).build());

            products.add(product);

            productStats.add(stats);

        }

        @Test
        @DisplayName("Success; bring all product with string like")
        public void shouldRetrieveProductLikeName_WhenRequestIsValid(){

            // ARRANGE

            String nameLike = "ap";

            // ACT

            Page<ProductCardResponse> response = productService.getProductLikeName(nameLike, this.page);

            // ASSERT

            assertProductCardResponse(response.getContent().get(0), this.productStats.get(0));

            assertProductCardResponse(response.getContent().get(1), this.productStats.get(1));

            assertProductCardResponse(response.getContent().get(2), this.productStats.get(2));

            assertPaginationContent(response);

        }

        @Test
        @DisplayName("Success; bring nothing when string like does not match anything")
        public void shouldRetrieveNothing_WhenStringDoesNotMatchWithAnything(){

            // ARRANGE

            String name = "Carrot";

            // ACT

            Page<ProductCardResponse> response = productService.getProductLikeName(name, this.page);

            // ASSERT

            assertThat(response.getContent()).isEmpty();

            assertThat(response.getTotalElements()).isZero();

            assertThat(response.getTotalPages()).isZero();

        }

        public void assertPaginationContent(Page<ProductCardResponse> response){

            assertThat(response.getContent())
                    .extracting(ProductCardResponse::name)
                    .containsExactly("Apple", "Apricot", "Aprium")
                    .doesNotContain("Banana", "Onion");

            assertThat(response.getContent()).hasSize(3);

            assertThat(response.getTotalElements()).isEqualTo(3);

            assertThat(response.getTotalPages()).isEqualTo(1);

            assertThat(response.getNumber()).isEqualTo(0);

        }




    }

    @Nested
    @DisplayName("Register Product")
    public class RegisterProductTest{

        User user;
        List<Category> categories = new ArrayList<>();

        @BeforeEach
        public void setUp(){

            this.user = userRepository.saveAndFlush(UserFixture.builder().build());

            setUpCategories("Food", CategoryStatus.ACTIVE);

            setUpCategories("Fruit", CategoryStatus.ACTIVE);

        }

        public void setUpCategories(String name, CategoryStatus status){

            Category category = categoryRepository.saveAndFlush(CategoryFixture.builder().name(name).status(status).build());

            categories.add(category);
        }

        @Test
        @DisplayName("Success; register product")
        public void shouldRegisterProduct_WhenRequestIsValid(){

            // ARRANGE

            ProductRegisterRequest request = ProductDTOFixture.productRegisterRequest(this.user);

            // ACT

            ProductFoundResponse response = productService.registerProduct(request, this.user.getId());

            // ASSERT

            assertResponseMatchesRequest(response, request);

            ProductStats stats = productStatsQuery.findByProductIdWithProduct(response.id());

            assertProductFoundResponse(response, stats.getProduct());

            assertProductStatsResponse(response.stats(), stats);

            assertThat(response.categories())
                    .extracting(CategoryResponse::name)
                    .containsExactly("Food")
                    .doesNotContain("Fruit");

            assertCategoryResponse(response.categories().get(0), this.categories.get(0));

        }


        @Test
        @DisplayName("Failure; product name already exist")
        public void shouldThrowException_WhenProductNameAlreadyExist(){

            // ARRANGE

            productRepository.saveAndFlush(ProductFixture.builder().vendor(this.user).build());

            ProductRegisterRequest request = ProductDTOFixture.productRegisterRequest(this.user);

            // ACT & ASSERT

            assertThatThrownBy(() -> productService.registerProduct(request, this.user.getId()))
                    .isInstanceOf(ProductException.DuplicateName.class);

        }

        @Test
        @DisplayName("Failure; category selected does not exists")
        public void shouldThrowException_WhenCategoryDoesNotExist(){

            // ARRANGE

            Long nonExistingCategoryId = 999L;

            ProductRegisterRequest request = ProductDTOFixture.productRegisterRequest(this.user, nonExistingCategoryId);

            // ACT & ASSERT

            assertThatThrownBy(() -> productService.registerProduct(request, this.user.getId()))
                    .isInstanceOf(CategoryException.NotFound.class);

        }

        public void assertResponseMatchesRequest(ProductFoundResponse response, ProductRegisterRequest request){

            assertThat(request.name()).isEqualTo(response.name());

            assertThat(request.price()).isEqualTo(response.price());

            assertThat(request.stock()).isEqualTo(response.stock());

        }


    }

    @Nested
    @DisplayName("Make Product Unavailable")
    public class MakeProductUnavailable{

        User user;
        Product product;

        @BeforeEach
        public void setUp(){

            this.user = userRepository.saveAndFlush(UserFixture.builder().build());



        }

        @Test
        @DisplayName("Success; product status is changed to unavailable")
        public void shouldChangeProductStateToUnavailable_WhenRequestIsValid(){

            // ARRANGE

            this.product = productRepository.saveAndFlush(ProductFixture.builder().vendor(this.user).status(ProductStatus.AVAILABLE).build());

            // ACT

            ProductStatusResponse response = productService.toggleProductAvailabilityStatus(product.getId(), this.user.getId());

            // ASSERT

            assertThat(response.id()).isEqualTo(product.getId());

            assertThat(response.status()).isEqualTo(ProductStatus.UNAVAILABLE);

        }

        @Test
        @DisplayName("Success; product status is changed to available")
        public void shouldChangeProductStateToAvailable_WhenRequestIsValid(){

            // ARRANGE

            this.product = productRepository.saveAndFlush(ProductFixture.builder().vendor(this.user).status(ProductStatus.UNAVAILABLE).build());


            // ACT

            ProductStatusResponse response = productService.toggleProductAvailabilityStatus(product.getId(), this.user.getId());

            // ASSERT

            assertThat(response.id()).isEqualTo(product.getId());

            assertThat(response.status()).isEqualTo(ProductStatus.AVAILABLE);

        }

    }

    @Nested
    @DisplayName("Delete Product")
    public class DeleteProductTest{

        @Test
        @DisplayName("Success; product status changed to deleted")
        public void shouldProductChangeToDeleted_WhenRequestIsValid(){

            // ARRANGE

            User user = userRepository.saveAndFlush(UserFixture.builder().build());

            Product product = productRepository.saveAndFlush(ProductFixture.builder().vendor(user).build());

            // ACT

            productService.deleteProduct(product.getId(), user.getId());

            // ASSERT

            Product deletedProduct = productQuery.findById(product.getId());

            assertThat(deletedProduct.getStatus()).isEqualTo(ProductStatus.DELETED);

        }


    }

    @Nested
    @DisplayName("Increase Product Stock")
    public class IncreaseProductStock{

        User user;
        Product product;

        Integer quantity = 2;

        @BeforeEach
        public void setUp(){

            this.user = userRepository.saveAndFlush(UserFixture.builder().build());

        }

        @Test
        @DisplayName("Success; product stock is increased and status remains unchanged")
        public void shouldProductStockIncrease_WhenRequestIsValid(){

            // ARRANGE

            this.product = productRepository.saveAndFlush(ProductFixture.builder().vendor(this.user).stock(10).status(ProductStatus.UNAVAILABLE).build());

            ProductStockIncreaseRequest request = ProductDTOFixture.productStockIncreaseRequest(this.quantity);

            Integer expectedQuantity = this.product.getStock() + this.quantity;

            // ACT

            ProductStockResponse response = productService.increaseProductStock(this.product.getId(), this.user.getId(), request);

            // ASSERT

            Product updatedProduct = productQuery.findById(this.product.getId());

            assertProductStockResponse(response, updatedProduct, expectedQuantity);

            assertThat(updatedProduct.getStatus()).isEqualTo(ProductStatus.UNAVAILABLE);

        }

        @Test
        @DisplayName("Success; product stock is increased and changed status from out_of_stock to available")
        public void shouldProductStockIncreaseAndChangeStatus_WhenRequestIsValid(){

            // ARRANGE

            this.product = productRepository.saveAndFlush(ProductFixture.builder().vendor(this.user).stock(0).status(ProductStatus.OUT_OF_STOCK).build());

            ProductStockIncreaseRequest request = ProductDTOFixture.productStockIncreaseRequest(this.quantity);

            Integer expectedQuantity = this.product.getStock() + this.quantity;

            // ACT

            ProductStockResponse response = productService.increaseProductStock(this.product.getId(), this.user.getId(), request);

            // ASSERT

            Product updatedProduct = productQuery.findById(this.product.getId());

            assertProductStockResponse(response, updatedProduct, expectedQuantity);

            assertThat(updatedProduct.getStatus()).isEqualTo(ProductStatus.AVAILABLE);

        }

        public void assertProductStockResponse(ProductStockResponse response, Product updatedProduct, Integer expectedQuantity){

            assertThat(response.id()).isEqualTo(this.product.getId());

            assertThat(response.stock()).isEqualTo(expectedQuantity);

            assertThat(updatedProduct.getStock()).isEqualTo(expectedQuantity);

        }

    }

    @Nested
    @DisplayName("Make Product Available")
    public class MakeProductAvailable{

        User user;
        Product product;

        @BeforeEach
        public void setUp(){

            this.user = userRepository.saveAndFlush(UserFixture.builder().build());

        }

        @Test
        @DisplayName("Success; product status changed to out_of_stock")
        public void shouldChangedStatusToOutOfStock_WhenRequestIsValid(){

            // ARRANGE

            this.product = productRepository.saveAndFlush(ProductFixture.builder().vendor(this.user).status(ProductStatus.WAITING_APPROVAL).stock(0).build());

            // ACT

            ProductStatusResponse response = productService.approveProduct(this.product.getId());

            // ASSERT

            assertThat(response.id()).isEqualTo(this.product.getId());

            assertThat(response.status()).isEqualTo(ProductStatus.OUT_OF_STOCK);

            Product updateProduct = productQuery.findById(this.product.getId());

            assertProductStatusResponse(response, updateProduct);

        }

        @Test
        @DisplayName("Success; product status changed to available")
        public void shouldChangeStatusToAvailable_WhenRequestIsValid(){

            // ARRANGE

            this.product = productRepository.saveAndFlush(ProductFixture.builder().vendor(this.user).status(ProductStatus.WAITING_APPROVAL).stock(1).build());

            // ACT

            ProductStatusResponse response = productService.approveProduct(this.product.getId());

            // ASSERT

            assertThat(response.id()).isEqualTo(this.product.getId());

            assertThat(response.status()).isEqualTo(ProductStatus.AVAILABLE);

            Product updateProduct = productQuery.findById(this.product.getId());

            assertProductStatusResponse(response, updateProduct);

        }
    }
}
