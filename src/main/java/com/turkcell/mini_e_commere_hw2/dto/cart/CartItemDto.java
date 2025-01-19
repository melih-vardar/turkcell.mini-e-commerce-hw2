package com.turkcell.mini_e_commere_hw2.dto.cart;

import lombok.*;

@Getter
@Setter
@Builder
public class CartItemDto {
    private Integer id;
    private Integer productId;
    private int quantity;
    private int price;

    public CartItemDto(Integer id, Integer productId, int quantity, int price) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }
}