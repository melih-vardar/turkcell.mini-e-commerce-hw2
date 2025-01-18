package com.turkcell.mini_e_commere_hw2.dto.cart;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {
    private Integer id;
    private Integer productId;
    private int quantity;
    private int price;
} 