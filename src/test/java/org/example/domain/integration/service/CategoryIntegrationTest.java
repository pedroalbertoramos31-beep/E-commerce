package org.example.domain.integration.service;

import jakarta.transaction.Transactional;
import org.example.domain.category.*;
import org.example.domain.category.dto.request.CategoryRegisterRequest;
import org.example.domain.category.dto.response.CategoryResponse;
import org.example.domain.factory.CategoryTestData;
import org.example.infrastructure.exception.error.CategoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@SpringBootTest
@Transactional
public class CategoryIntegrationTest {

    @Autowired CategoryRepository categoryRepository;
    @Autowired CategoryService categoryService;

    @Autowired
    CategoryQuery categoryQuery;

    @Nested
    @DisplayName("Register Category")
    class RegisterCategory{

        @Test
        @DisplayName("Success; create category")
        public void shouldCreateCategory_WhenRequestIsValid() {

            // ARRANGE

            CategoryRegisterRequest request = CategoryTestData.categoryRegisterRequest();

            CategoryStatus activeStatus = CategoryStatus.ACTIVE;

            // ACT

            CategoryResponse response = categoryService.registerCategory(request);

            // ASSERT - Response

            assertThat(response.name()).isEqualTo(request.name());

            assertThat(response.status()).isEqualTo(activeStatus);

            // ASSERT - Persistence

            Category savedCategory = categoryQuery.findById(response.id());

            assertThat(savedCategory.getName()).isEqualTo(request.name());

            assertThat(savedCategory.getStatus()).isEqualTo(activeStatus);
        }

        @Test
        @DisplayName("Failure; category name already exists")
        public void shouldThrowException_WhenCategoryNameAlreadyExists() {

            // ARRANGE

            CategoryRegisterRequest request = CategoryTestData.categoryRegisterRequest();

            categoryService.registerCategory(request);

            // ACT & ASSERT

            assertThatThrownBy(() -> categoryService.registerCategory(request))
                    .isInstanceOf(CategoryException.DuplicateName.class);

        }
    }

    @Nested
    @DisplayName("Update Category State")
    class UpdateCategoryState{

        Category category;

        @BeforeEach
        void setUp(){

            this.category = categoryRepository.saveAndFlush(CategoryTestData.simpleCategory());

        }

        @Test
        @DisplayName("Success; category status is updated")
        public void shouldUpdateCategoryState_WhenRequestIsValid(){

            // ARRANGE

            CategoryStatus inactiveStatus = CategoryStatus.INACTIVE;

            // ACT

            CategoryResponse response = categoryService.updateCategoryState(this.category.getId(), inactiveStatus);

            // ASSERT - Response

            assertThat(response.id()).isEqualTo(this.category.getId());

            assertThat(response.name()).isEqualTo(this.category.getName());

            assertThat(response.status()).isEqualTo(inactiveStatus);

            // ASSERT - Persistence

            Category persisted = categoryQuery.findById(response.id());

            assertThat(persisted.getStatus()).isEqualTo(inactiveStatus);

        }

        @Test
        @DisplayName("Failure; category id does not exist")
        public void shouldThrowException_WhenCategoryIdDoesNotExist(){

            // ARRANGE

            CategoryStatus inactiveStatus = CategoryStatus.INACTIVE;

            Long nonExistingId = -1L;

            // ACT & ASSERT

            assertThatThrownBy( () -> categoryService.updateCategoryState(nonExistingId, inactiveStatus))
                    .isInstanceOf(CategoryException.NotFound.class);

        }
    }

}
