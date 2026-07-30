package org.example.domain.fixture.dto;

import org.example.domain.fixture.entity.ProductFixture;
import org.example.domain.product.dto.request.ProductRegisterRequest;
import org.example.domain.product.dto.request.ProductStockIncreaseRequest;
import org.example.domain.user.User;

import java.util.Set;

public class ProductDTOFixture {

    public static ProductRegisterRequest productRegisterRequest(User user){
        return new ProductRegisterRequest(
                ProductFixture.DEFAULT_NAME,
                ProductFixture.DEFAULT_PRICE,
                ProductFixture.DEFAULT_STOCK,
                ProductFixture.DEFAULT_CATEGORY
        );
    }

    public static ProductRegisterRequest productRegisterRequest(User user, Long categoryId){
        return new ProductRegisterRequest(
                ProductFixture.DEFAULT_NAME,
                ProductFixture.DEFAULT_PRICE,
                ProductFixture.DEFAULT_STOCK,
                Set.of(categoryId)
        );
    }

    public static ProductStockIncreaseRequest productStockIncreaseRequest(Integer quantity){
        return new ProductStockIncreaseRequest(quantity);
    }
}
