package org.example.domain.fixture.entity;

import org.example.domain.product.Product;
import org.example.domain.product.ProductStatus;
import org.example.domain.user.User;

import java.math.BigDecimal;
import java.util.Set;

public class ProductFixture {

    public static final String DEFAULT_NAME = "Apple";
    public static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(1);
    public static final Integer DEFAULT_STOCK = 10;
    public static final ProductStatus DEFAULT_STATUS = ProductStatus.AVAILABLE;
    public static final Set<Long> DEFAULT_CATEGORY = Set.of(1L);

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static class ProductBuilder {

        private String name = DEFAULT_NAME;
        private BigDecimal price = DEFAULT_PRICE;
        private Integer stock = DEFAULT_STOCK;
        private ProductStatus status = DEFAULT_STATUS;

        private User vendor = UserFixture.builder().build();

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductBuilder stock(Integer stock) {
            this.stock = stock;
            return this;
        }

        public ProductBuilder status(ProductStatus status) {
            this.status = status;
            return this;
        }

        public ProductBuilder vendor(User vendor) {
            this.vendor = vendor;
            return this;
        }

        public Product build() {

            Product product = Product.create(name, price, stock, vendor);

            product.changeState(status);

            return product;
        }
    }


}
