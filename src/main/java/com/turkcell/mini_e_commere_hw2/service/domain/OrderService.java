package com.turkcell.mini_e_commere_hw2.service.domain;

import com.turkcell.mini_e_commere_hw2.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order createOrder(UUID userId);

    Order updateOrderState(Integer id);

    List<Order> getAllUserOrders(UUID id);

    Order getOrderById(Integer id);

    void deleteOrder(Integer orderId);
}
