package org.example.domain.factory;

import org.example.domain.product.Product;
import org.example.domain.product_stats.ProductStats;

public class ProductStatsTestData {

    public static final Double DEFAULT_AVG_RATING = 0.0;
    public static final Integer DEFAULT_REVIEW_COUNT = 0;
    public static final Integer DEFAULT_SALES_COUNT = 0;

    public static ProductStats simpleProductStats(Product product){
        return ProductStats.create(product);
    }




}
