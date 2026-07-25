package org.example.domain.integration.query;

import jakarta.transaction.Transactional;
import org.example.domain.cart.CartRepository;
import org.example.domain.cart_item.CartItemRepository;
import org.example.domain.product.ProductQuery;
import org.example.domain.product.ProductRepository;
import org.example.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class CartQueryTest {

    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private CartRepository cartRepository;

    @Autowired private ProductQuery productQuery;





}


