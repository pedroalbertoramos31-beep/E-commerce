package org.example.domain.product_category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCategoryQuery {

    private final ProductCategoryRepository productCategoryRepository;

    public List<ProductCategory> getByProductIdWithCategory(Long productId){
        return productCategoryRepository.getWithCategory(productId);
    }

}
