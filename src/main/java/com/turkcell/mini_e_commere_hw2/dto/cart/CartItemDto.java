package com.turkcell.mini_e_commere_hw2.dto.cart;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Integer id;
    private Integer productId;
    private int quantity;
    private BigDecimal price;
}