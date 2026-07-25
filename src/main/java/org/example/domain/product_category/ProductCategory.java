package org.example.domain.product_category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.domain.category.Category;
import org.example.domain.product.Product;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "product_category", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_product_category",
                columnNames = {"product_id", "category_id"}
        )
})
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "category_id")
    private Category category;



    public static ProductCategory create(Product product, Category category){

        ProductCategory productCategory = new ProductCategory();

        productCategory.product = product;
        productCategory.category = category;

        return productCategory;

    }


}
