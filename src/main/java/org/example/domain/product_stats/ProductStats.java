package org.example.domain.product_stats;


import jakarta.persistence.*;
import lombok.*;
import org.example.domain.product.Product;

@Entity
@Table(name = "product_stats")
@Getter
@Setter
@NoArgsConstructor
public class ProductStats {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "average_rating", nullable = false)
    private Double averageRating = 0.0;

    @Column(name = "review_count", nullable = false)
    private Integer reviewCount = 0;

    @Column(name = "sales_count", nullable = false)
    private Integer salesCount = 0;

    public static ProductStats create(Product product){

        ProductStats productStats = new ProductStats();

        productStats.product = product;

        return productStats;

    }

    public void updateRating(Integer reviewRating) {

        double newAvg = ((this.averageRating * this.reviewCount) + reviewRating)
                / (this.reviewCount + 1);

        this.averageRating = Math.round(newAvg * 10.0) / 10.0;
    }

    public void updateSalesCount(Integer quantity){
        this.salesCount += quantity;
    }

    public void updateReviewCount(Integer quantity){
        this.reviewCount += quantity;
    }

}
