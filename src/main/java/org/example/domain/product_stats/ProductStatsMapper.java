package org.example.domain.product_stats;

import org.example.domain.product_stats.dto.response.ProductStatsResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductStatsMapper {

    ProductStatsResponse toProductStatsResponse(ProductStats productStats);

}
