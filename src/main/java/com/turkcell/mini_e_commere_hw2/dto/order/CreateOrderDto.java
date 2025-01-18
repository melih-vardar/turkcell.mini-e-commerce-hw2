package com.turkcell.mini_e_commere_hw2.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderDto {
    @NotNull(message = "User ID cannot be null")
    private UUID userId;
    private List<OrderItemDto> orderItems;
}
