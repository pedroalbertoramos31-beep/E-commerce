package org.example.domain.fixture.dto;

import org.example.domain.category.dto.request.CategoryRegisterRequest;
import org.example.domain.fixture.entity.CategoryFixture;

public class CategoryDTOFixture {


    public static CategoryRegisterRequest categoryRegisterRequest(){
        return new CategoryRegisterRequest(CategoryFixture.DEFAULT_NAME);
    }

}
