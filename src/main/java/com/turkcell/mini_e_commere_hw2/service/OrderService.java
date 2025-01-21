package com.turkcell.mini_e_commere_hw2.service;

import com.turkcell.mini_e_commere_hw2.dto.order.OrderListingDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderListingDto createOrder(UUID userId);
    OrderListingDto updateOrderState(Integer id);
    List<OrderListingDto> getAllUserOrders(UUID id);
}
