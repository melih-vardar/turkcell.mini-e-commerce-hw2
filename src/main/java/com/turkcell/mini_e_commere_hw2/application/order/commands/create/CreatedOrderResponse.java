package com.turkcell.mini_e_commere_hw2.application.order.commands.create;

import com.turkcell.mini_e_commere_hw2.dto.order.OrderItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CreatedOrderResponse {
    private Integer id;
    private UUID userId;
    private List<OrderItemDto> orderItems;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
    private String status;
} 