package org.example.domain.product_category.dto.response;

import org.example.domain.category.Category;
import org.example.domain.product.Product;

public record ProductCategoryEntityResponse(
        Long id,
        Product product,
        Category category
) {
}
