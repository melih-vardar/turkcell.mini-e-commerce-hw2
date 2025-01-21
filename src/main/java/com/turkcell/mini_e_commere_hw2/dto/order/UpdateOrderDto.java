package com.turkcell.mini_e_commere_hw2.dto.order;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderDto {
    private Integer id;
    private List<OrderItemDto> orderItems;
}
