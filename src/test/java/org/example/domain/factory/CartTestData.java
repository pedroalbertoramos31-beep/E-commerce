package org.example.domain.factory;

import org.example.domain.cart.Cart;
import org.example.domain.user.User;

public class CartTestData {

    public static Cart simpleCart(User user){
        return Cart.create(user);

    }
}
