package com.turkcell.mini_e_commere_hw2.dto.order;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderListingDto {
    private Integer id;
    private UUID userId;
    private List<OrderItemDto> orderItems;
    private BigDecimal totalPrice;
}
