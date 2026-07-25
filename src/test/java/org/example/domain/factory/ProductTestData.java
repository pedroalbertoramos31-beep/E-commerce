package org.example.domain.factory;

import org.example.domain.product.Product;
import org.example.domain.product.ProductStatus;
import org.example.domain.product.dto.request.ProductRegisterRequest;
import org.example.domain.product.dto.request.ProductStockIncreaseRequest;
import org.example.domain.user.User;

import java.math.BigDecimal;
import java.util.Set;

public class ProductTestData {

    public static final String DEFAULT_NAME = "Apple";
    public static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(1);
    public static final Integer DEFAULT_STOCK = 10;
    public static final ProductStatus DEFAULT_STATUS = ProductStatus.AVAILABLE;
    public static final Set<Long> DEFAULT_CATEGORY = Set.of(1L);

    public static ProductBuilder customProduct(User user) {
        return new ProductBuilder(user);
    }

    public static Product simpleProduct(User user) {
        return new ProductBuilder(user).build();
    }

    public static class ProductBuilder {

        private String name = DEFAULT_NAME;
        private BigDecimal price = DEFAULT_PRICE;
        private Integer stock = DEFAULT_STOCK;
        private ProductStatus status = DEFAULT_STATUS;

        private final User user;

        private ProductBuilder(User user) {
            this.user = user;
        }

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

        public Product build() {

            Product product = Product.create(name, price, stock, user);
            product.changeState(status);

            return product;
        }
    }

    public static ProductRegisterRequest productRegisterRequest(User user){
        return new ProductRegisterRequest(
                DEFAULT_NAME,
                DEFAULT_PRICE,
                DEFAULT_STOCK,
                DEFAULT_CATEGORY
        );
    }

    public static ProductRegisterRequest productRegisterRequest(User user, Long categoryId){
        return new ProductRegisterRequest(
                DEFAULT_NAME,
                DEFAULT_PRICE,
                DEFAULT_STOCK,
                Set.of(categoryId)
        );
    }

    public static ProductStockIncreaseRequest productStockIncreaseRequest(Integer quantity){
        return new ProductStockIncreaseRequest(quantity);
    }

}
