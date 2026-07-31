package org.example.domain.assertion.service;

import org.example.domain.product.Product;
import org.example.domain.product.dto.response.ProductCardResponse;
import org.example.domain.product.dto.response.ProductFoundResponse;
import org.example.domain.product.dto.response.ProductStatusResponse;
import org.example.domain.product_stats.ProductStats;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.domain.assertion.service.ProductStatsTestAssertion.assertProductStatsResponse;

public class ProductTestAssertion {

    public static void assertProductFoundResponse(ProductFoundResponse response, Product product){

        assertThat(response.id()).isEqualTo(product.getId());

        assertThat(response.name()).isEqualTo(product.getName());

        assertThat(response.price()).isEqualTo(product.getPrice());

        assertThat(response.stock()).isEqualTo(product.getStock());

        assertThat(response.status()).isEqualTo(product.getStatus());

        assertThat(response.vendor().getId()).isEqualTo(product.getVendor().getId());

    }

    public static void assertProductStatusResponse(ProductStatusResponse response, Product product){

        assertThat(response.id()).isEqualTo(product.getId());

        assertThat(response.status()).isEqualTo(product.getStatus());

    }


    public static void assertProductCardResponse(ProductCardResponse response, ProductStats stats){

        assertThat(response.id()).isEqualTo(stats.getProduct().getId());

        assertThat(response.name()).isEqualTo(stats.getProduct().getName());

        assertProductStatsResponse(response.stats(), stats);

    }

}
