package org.example.domain.factory;

import org.example.domain.category.Category;
import org.example.domain.product.Product;
import org.example.domain.product_category.ProductCategory;

public class ProductCategoryTestData {

    public static ProductCategory simpleProductCategory(Product product, Category category){
        return ProductCategory.create(product, category);
    }

}
