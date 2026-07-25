package org.example.domain.assertion;

import org.example.domain.category.Category;
import org.example.domain.category.dto.response.CategoryResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CategoryTestAssertion {

    public static void assertCategoryResponse(CategoryResponse categoryResponse, Category category){

        assertThat(categoryResponse.id()).isEqualTo(category.getId());

        assertThat(categoryResponse.name()).isEqualTo(category.getName());

        assertThat(categoryResponse.status()).isEqualTo(category.getStatus());

    }

}
