package org.example.domain.category;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.domain.category.dto.response.CategoryResponse;
import org.example.domain.product_category.ProductCategory;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-21T12:25:35-0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.12 (Eclipse Adoptium)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryResponse toCategoryResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        CategoryStatus status = null;

        id = category.getId();
        name = category.getName();
        status = category.getStatus();

        CategoryResponse categoryResponse = new CategoryResponse( id, name, status );

        return categoryResponse;
    }

    @Override
    public List<CategoryResponse> toCategoryResponseList(List<Category> categories) {
        if ( categories == null ) {
            return null;
        }

        List<CategoryResponse> list = new ArrayList<CategoryResponse>( categories.size() );
        for ( Category category : categories ) {
            list.add( toCategoryResponse( category ) );
        }

        return list;
    }

    @Override
    public CategoryResponse toCategoryResponseFromProductCategory(ProductCategory category) {
        if ( category == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        CategoryStatus status = null;

        id = categoryCategoryId( category );
        name = categoryCategoryName( category );
        status = categoryCategoryStatus( category );

        CategoryResponse categoryResponse = new CategoryResponse( id, name, status );

        return categoryResponse;
    }

    @Override
    public List<CategoryResponse> toCategoryResponseListFromProductCategory(List<ProductCategory> categories) {
        if ( categories == null ) {
            return null;
        }

        List<CategoryResponse> list = new ArrayList<CategoryResponse>( categories.size() );
        for ( ProductCategory productCategory : categories ) {
            list.add( toCategoryResponseFromProductCategory( productCategory ) );
        }

        return list;
    }

    private Long categoryCategoryId(ProductCategory productCategory) {
        Category category = productCategory.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getId();
    }

    private String categoryCategoryName(ProductCategory productCategory) {
        Category category = productCategory.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getName();
    }

    private CategoryStatus categoryCategoryStatus(ProductCategory productCategory) {
        Category category = productCategory.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getStatus();
    }
}
