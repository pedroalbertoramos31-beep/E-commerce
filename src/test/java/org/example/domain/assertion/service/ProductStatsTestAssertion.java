package org.example.domain.assertion.service;

import org.example.domain.product_stats.ProductStats;
import org.example.domain.product_stats.dto.response.ProductStatsResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductStatsTestAssertion {

    public static void assertProductStatsResponse(ProductStatsResponse response, ProductStats stats){

        assertThat(response.id()).isEqualTo(stats.getId());

        assertThat(response.averageRating()).isEqualTo(stats.getAverageRating());

        assertThat(response.reviewCount()).isEqualTo(stats.getReviewCount());

        assertThat(response.salesCount()).isEqualTo(stats.getSalesCount());

    }

}
