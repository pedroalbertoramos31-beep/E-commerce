package org.example.domain.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.domain.audit.Auditable;
import org.example.domain.user.User;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "products")
public class Product extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status = ProductStatus.WAITING_APPROVAL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private User vendor;

    /* CONSTRUCTOR */

    public static Product create(String name, BigDecimal price, Integer stock, User user){

        Product product = new Product();

        product.name = name;
        product.price = price;
        product.stock = stock;
        product.vendor = user;

        return product;
    }

    /* METHODS */

    public void changeState(ProductStatus state){
        this.status = state;
    }

    public void decreaseStock(Integer quantity){

        if (quantity > this.stock){
            throw new RuntimeException("There is not enough stock");
        }

        this.stock -= quantity;

        if (this.stock == 0){
            this.status = ProductStatus.OUT_OF_STOCK;
        }
    }

    public void increaseStock(Integer quantity){
        this.stock += quantity;

        if (this.stock > 0 && this.status == ProductStatus.OUT_OF_STOCK){
            this.status = ProductStatus.AVAILABLE;
        }
    }



}





