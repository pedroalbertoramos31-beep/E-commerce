package org.example.domain.fixture.entity;

import org.example.domain.product.Product;
import org.example.domain.product_stats.ProductStats;

public class ProductStatsFixture {


    public static ProductStatsBuilder builder() {
        return new ProductStatsBuilder();
    }

    public static class ProductStatsBuilder {

        private Product product = ProductFixture.builder().build();

        public ProductStatsBuilder product(Product product) {
            this.product = product;
            return this;
        }

        public ProductStats build() {
            return ProductStats.create(product);
        }
    }





}
