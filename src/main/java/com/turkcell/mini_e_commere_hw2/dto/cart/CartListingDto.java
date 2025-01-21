package com.turkcell.mini_e_commere_hw2.dto.cart;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartListingDto {
    private Integer id;
    private UUID userId;
    private List<CartItemDto> cartItems;
    private BigDecimal totalPrice;
}
