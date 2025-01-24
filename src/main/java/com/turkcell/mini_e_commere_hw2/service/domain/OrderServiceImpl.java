package com.turkcell.mini_e_commere_hw2.service.domain;
import com.turkcell.mini_e_commere_hw2.entity.Order;
import com.turkcell.mini_e_commere_hw2.entity.OrderItem;
import com.turkcell.mini_e_commere_hw2.enums.OrderStatus;
import com.turkcell.mini_e_commere_hw2.entity.User;
import com.turkcell.mini_e_commere_hw2.repository.OrderRepository;
import com.turkcell.mini_e_commere_hw2.rules.OrderBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderBusinessRules orderBusinessRules;
    private final UserService userService;
    private final CartService cartService;

    private static final LinkedList<OrderStatus> ORDER_STATUS_PROGRESSION = new LinkedList<>();

    static {
        ORDER_STATUS_PROGRESSION.add(OrderStatus.PREPARING);
        ORDER_STATUS_PROGRESSION.add(OrderStatus.SHIPPED);
        ORDER_STATUS_PROGRESSION.add(OrderStatus.DELIVERED);
    }

    @Override
    public Order createOrder(UUID userId) {
        User user = userService.getById(userId);
        orderBusinessRules.cartMustNotBeEmpty(user.getCart());
        orderBusinessRules.checkTheProductStockAfterUpdateProductStockForOrder(user.getCart());

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PREPARING);
        order.setTotalPrice(user.getCart().getTotalPrice());


        List<OrderItem> orderItems = user.getCart().getCartItems().stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getProduct().getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);

        cartService.resetCart(user.getCart().getId());
        orderRepository.save(order);

        return order;
    }

    @Override
    public Order updateOrderState(Integer id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new BusinessException("Order not found"));
        OrderStatus currentStatus = order.getStatus();
        handleOrderStatusTransition(currentStatus, order);
        orderRepository.save(order);

        return order;
    }

    @Override
    public List<Order> getAllUserOrders(UUID userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }

    @Override
    public Order getOrderById(Integer id) {
        return orderRepository.findById(id).orElseThrow(() -> new BusinessException("Order not found"));
    }

    @Override
    public void deleteOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new BusinessException("Order not found"));
        orderRepository.delete(order);
    }

    private void handleOrderStatusTransition(OrderStatus currentStatus, Order order) {
        int currentIndex = ORDER_STATUS_PROGRESSION.indexOf(currentStatus);
        
        if (currentIndex == -1 || currentIndex == ORDER_STATUS_PROGRESSION.size() - 1) {
            if (currentStatus == OrderStatus.DELIVERED) {
                throw new BusinessException("Order already delivered. No further status updates allowed.");
            }
            throw new BusinessException("Invalid order status transition.");
        }
        
        OrderStatus nextStatus = ORDER_STATUS_PROGRESSION.get(currentIndex + 1);
        order.setStatus(nextStatus);
    }
}
