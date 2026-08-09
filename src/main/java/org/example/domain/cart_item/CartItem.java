package org.example.domain.cart_item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.audit.Auditable;
import org.example.domain.cart.Cart;
import org.example.domain.product.Product;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "cart_item",
        uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_cart_product",
                columnNames = {"cart_id", "product_id"}
        )
        }
    )

public class CartItem extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    /* CONSTRUCTOR */

    public static CartItem create(Integer quantity, Product product, Cart cart){

        CartItem cartItem = new CartItem();

        cartItem.quantity = quantity;
        cartItem.product = product;
        cartItem.cart = cart;

        return cartItem;
    }


    /* METHODS */

    public void changeQuantity(Integer quantity){
        this.quantity = quantity;
    }

    public void addQuantity(Integer quantity){
        this.quantity += quantity;
    }

}
