package org.example.domain.product;

import org.example.domain.category.CategoryMapper;
import org.example.domain.product.dto.response.*;
import org.example.domain.product_category.ProductCategory;
import org.example.domain.product_stats.ProductStats;
import org.example.domain.product_stats.ProductStatsMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, ProductStatsMapper.class})
public interface ProductMapper {

    @Mapping(source = "product.id", target = "id")
    @Mapping(source = "stats", target = "stats")
    @Mapping(source = "categories", target = "categories")
    ProductFoundResponse toProductFoundResponse(Product product, ProductStats stats, List<ProductCategory> categories);

    @Mapping(source = "product.id", target = "id")
    @Mapping(source = "product.name", target = "name")
    @Mapping(source = "stats", target = "stats")
    ProductCardResponse toProductCardResponse(ProductStats stats);


    ProductStatusResponse toProductStatusResponse(Product product);





    ProductStockResponse toProductIncreasedStockResponse(Product product);


}
