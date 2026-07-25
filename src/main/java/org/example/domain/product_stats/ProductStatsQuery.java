package org.example.domain.product_stats;

import lombok.RequiredArgsConstructor;
import org.example.infrastructure.exception.error.ProductException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductStatsQuery {

    private final ProductStatsRepository productStatsRepository;

    public Page<ProductStats> getByProductNameWithProduct(String name, Pageable page){
        return productStatsRepository.getProductLikeName(page, name);
    }

    public List<ProductStats> getByProductIds(List<Long> productIds){
        return productStatsRepository.findByProductIdIn(productIds);
    }

    public ProductStats findById(Long statsId){
        return productStatsRepository.findById(statsId)
                .orElseThrow(() -> new ProductException.NotFound(statsId));
    }

    public ProductStats findByProductIdWithProduct(Long productId){
        return productStatsRepository.findProductStatsByIdWithProduct(productId)
                .orElseThrow(() -> new ProductException.NotFound(productId));
    }


}
