package org.example.domain.cart;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.audit.Auditable;
import org.example.domain.user.User;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "carts")
public class Cart extends Auditable {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    /* CONSTRUCTOR */

    public Cart(User user){
        this.user = user;
    }

    public static Cart create(User user){

        Cart cart = new Cart();

        cart.user = user;

        return cart;
    }

}