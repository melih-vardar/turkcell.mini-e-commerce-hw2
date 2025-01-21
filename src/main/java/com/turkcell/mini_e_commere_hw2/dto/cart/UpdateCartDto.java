package com.turkcell.mini_e_commere_hw2.dto.cart;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartDto {
    private Integer id;
    private List<CartItemDto> cartItems;
}
