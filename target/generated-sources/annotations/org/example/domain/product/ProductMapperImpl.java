package org.example.domain.product;

import java.math.BigDecimal;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.domain.category.CategoryMapper;
import org.example.domain.category.dto.response.CategoryResponse;
import org.example.domain.product.dto.response.ProductCardResponse;
import org.example.domain.product.dto.response.ProductFoundResponse;
import org.example.domain.product.dto.response.ProductStatusResponse;
import org.example.domain.product.dto.response.ProductStockResponse;
import org.example.domain.product_category.ProductCategory;
import org.example.domain.product_stats.ProductStats;
import org.example.domain.product_stats.ProductStatsMapper;
import org.example.domain.product_stats.dto.response.ProductStatsResponse;
import org.example.domain.user.User;
import org.example.domain.user.dto.response.UserSummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-21T12:25:35-0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.12 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ProductStatsMapper productStatsMapper;

    @Override
    public ProductFoundResponse toProductFoundResponse(Product product, ProductStats stats, List<ProductCategory> categories) {
        if ( product == null && stats == null && categories == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        BigDecimal price = null;
        Integer stock = null;
        ProductStatus status = null;
        UserSummaryResponse vendor = null;
        if ( product != null ) {
            id = product.getId();
            name = product.getName();
            price = product.getPrice();
            stock = product.getStock();
            status = product.getStatus();
            vendor = userToUserSummaryResponse( product.getVendor() );
        }
        ProductStatsResponse stats1 = null;
        stats1 = productStatsMapper.toProductStatsResponse( stats );
        List<CategoryResponse> categories1 = null;
        categories1 = categoryMapper.toCategoryResponseListFromProductCategory( categories );

        ProductFoundResponse productFoundResponse = new ProductFoundResponse( id, name, price, stock, status, vendor, categories1, stats1 );

        return productFoundResponse;
    }

    @Override
    public ProductCardResponse toProductCardResponse(ProductStats stats) {
        if ( stats == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        ProductStatsResponse stats1 = null;

        id = statsProductId( stats );
        name = statsProductName( stats );
        stats1 = productStatsMapper.toProductStatsResponse( stats );

        ProductCardResponse productCardResponse = new ProductCardResponse( id, name, stats1 );

        return productCardResponse;
    }

    @Override
    public ProductStatusResponse toProductStatusResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        Long id = null;
        ProductStatus status = null;

        id = product.getId();
        status = product.getStatus();

        ProductStatusResponse productStatusResponse = new ProductStatusResponse( id, status );

        return productStatusResponse;
    }

    @Override
    public ProductStockResponse toProductIncreasedStockResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        Long id = null;
        Integer stock = null;

        id = product.getId();
        stock = product.getStock();

        ProductStockResponse productStockResponse = new ProductStockResponse( id, stock );

        return productStockResponse;
    }

    protected UserSummaryResponse userToUserSummaryResponse(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String username = null;

        id = user.getId();
        username = user.getUsername();

        UserSummaryResponse userSummaryResponse = new UserSummaryResponse( id, username );

        return userSummaryResponse;
    }

    private Long statsProductId(ProductStats productStats) {
        Product product = productStats.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String statsProductName(ProductStats productStats) {
        Product product = productStats.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }
}
