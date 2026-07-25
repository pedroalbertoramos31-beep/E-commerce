package org.example.domain.category;

import org.example.domain.category.dto.response.CategoryResponse;
import org.example.domain.product_category.ProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toCategoryResponse(Category category);

    List<CategoryResponse> toCategoryResponseList(List<Category> categories);

    @Mapping(source = "category.id", target = "id")
    @Mapping(source = "category.name", target = "name")
    @Mapping(source = "category.status", target = "status")
    CategoryResponse toCategoryResponseFromProductCategory(ProductCategory category);

    @Mapping(source = "categories.id", target = "id")
    @Mapping(source = "categories.name", target = "name")
    @Mapping(source = "categories.status", target = "status")
    List<CategoryResponse> toCategoryResponseListFromProductCategory(List<ProductCategory> categories);


}
