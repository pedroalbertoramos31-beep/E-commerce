package org.example.domain.fixture.dto;

import org.example.domain.cart_item.dto.request.CartItemQuantityRequest;

public class CartItemDTOFixture {

    public static CartItemQuantityRequest cartItemUpsertRequest(Integer quantity){
        return new CartItemQuantityRequest(quantity);
    }

//    public static CartItemResponse simpleCartItemResponse(){
//
//        return new CartItemResponse(
//                1L,
//                1
//        )
//
//    }


}
