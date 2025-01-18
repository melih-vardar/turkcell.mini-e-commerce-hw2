package com.turkcell.mini_e_commere_hw2.dto.cart;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class CartListingDto {
    private Integer id;
    private UUID userId;
    private List<CartItemDto> cartItems;
    private BigDecimal totalPrice;

    public CartListingDto(Integer id, UUID userId, List<CartItemDto> cartItems, BigDecimal totalPrice) {
        this.id = id;
        this.userId = userId;
        this.cartItems = cartItems;
        this.totalPrice = totalPrice;
    }
}
