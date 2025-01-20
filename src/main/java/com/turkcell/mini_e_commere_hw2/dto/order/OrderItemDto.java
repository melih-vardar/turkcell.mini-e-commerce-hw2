package com.turkcell.mini_e_commere_hw2.dto.order;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {
    private Integer id;
    private Integer productId;
    private int quantity;
    private BigDecimal price;
} 