package org.example.domain.factory;

import org.example.domain.category.Category;
import org.example.domain.category.CategoryStatus;
import org.example.domain.category.dto.request.CategoryRegisterRequest;

public class CategoryTestData {

    public static final String DEFAULT_NAME = "Food";
    public static final CategoryStatus DEFAULT_STATE = CategoryStatus.ACTIVE;

    public static Category simpleCategory(){
        return Category.create(
                DEFAULT_NAME,
                DEFAULT_STATE
        );
    }

    public static Category customCategory(String name, CategoryStatus status){
        return Category.create(name, status);
    }


    public static CategoryRegisterRequest categoryRegisterRequest(){
        return new CategoryRegisterRequest(DEFAULT_NAME);
    }

}
