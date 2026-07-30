package org.example.domain.fixture.dto;

import org.example.domain.cart_item.dto.request.CartItemUpsertRequest;

public class CartItemDTOFixture {

    public static CartItemUpsertRequest cartItemUpsertRequest(Integer quantity){
        return new CartItemUpsertRequest(quantity);
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
