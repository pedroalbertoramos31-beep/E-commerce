package org.example.domain.fixture.entity;

import org.example.domain.category.Category;
import org.example.domain.product.Product;
import org.example.domain.product_category.ProductCategory;

public class ProductCategoryFixture {

    public static ProductCategoryBuilder builder() {
        return new ProductCategoryBuilder();
    }

    public static class ProductCategoryBuilder {

        private Product product = ProductFixture.builder().build();
        private Category category = CategoryFixture.builder().build();

        public ProductCategoryBuilder product(Product product) {
            this.product = product;
            return this;
        }

        public ProductCategoryBuilder category(Category category) {
            this.category = category;
            return this;
        }

        public ProductCategory build() {
            return ProductCategory.create(product, category);
        }
    }


}
