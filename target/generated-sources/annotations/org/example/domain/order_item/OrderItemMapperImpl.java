package org.example.domain.order_item;

import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.example.domain.product.Product;
import org.example.domain.product.ProductStatus;
import org.example.domain.product.dto.response.ProductResponse;
import org.example.domain.user.User;
import org.example.domain.user.dto.response.UserSummaryResponse;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-21T12:25:35-0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.12 (Eclipse Adoptium)"
)
@Component
public class OrderItemMapperImpl implements OrderItemMapper {

    @Override
    public OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        Long id = null;
        Integer quantity = null;
        BigDecimal purchasedAt = null;
        ProductResponse product = null;

        id = orderItem.getId();
        quantity = orderItem.getQuantity();
        purchasedAt = orderItem.getPurchasedAt();
        product = productToProductResponse( orderItem.getProduct() );

        OrderItemResponse orderItemResponse = new OrderItemResponse( id, quantity, purchasedAt, product );

        return orderItemResponse;
    }

    protected UserSummaryResponse userToUserSummaryResponse(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String username = null;

        id = user.getId();
        username = user.getUsername();

        UserSummaryResponse userSummaryResponse = new UserSummaryResponse( id, username );

        return userSummaryResponse;
    }

    protected ProductResponse productToProductResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        BigDecimal price = null;
        Integer stock = null;
        UserSummaryResponse vendor = null;

        id = product.getId();
        name = product.getName();
        price = product.getPrice();
        stock = product.getStock();
        vendor = userToUserSummaryResponse( product.getVendor() );

        ProductStatus state = null;

        ProductResponse productResponse = new ProductResponse( id, name, price, stock, state, vendor );

        return productResponse;
    }
}
