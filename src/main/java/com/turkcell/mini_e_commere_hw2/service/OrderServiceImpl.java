package com.turkcell.mini_e_commere_hw2.service;

import com.turkcell.mini_e_commere_hw2.dto.order.OrderItemDto;
import com.turkcell.mini_e_commere_hw2.dto.order.OrderListingDto;
import com.turkcell.mini_e_commere_hw2.entity.Cart;
import com.turkcell.mini_e_commere_hw2.entity.Order;
import com.turkcell.mini_e_commere_hw2.entity.OrderItem;
import com.turkcell.mini_e_commere_hw2.enums.OrderStatus;
import com.turkcell.mini_e_commere_hw2.entity.User;
import com.turkcell.mini_e_commere_hw2.repository.OrderItemRepository;
import com.turkcell.mini_e_commere_hw2.repository.OrderRepository;
import com.turkcell.mini_e_commere_hw2.rules.OrderBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderBusinessRules orderBusinessRules;
    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;

    @Override
    public OrderListingDto createOrder(UUID userId) {
        User user = userService.findById(userId);
        orderBusinessRules.cartMustNotBeEmpty(user.getCart());
        orderBusinessRules.checkTheProductStockAfterUpdateProductStockForOrder(user.getCart(), productService);

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.HAZIRLANIYOR);
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

        return new OrderListingDto(
                order.getId(),
                order.getUser().getId(),
                order.getOrderItems().stream().map(
                        item -> new OrderItemDto(
                                item.getId(),
                                item.getProduct().getId(),
                                item.getQuantity(),
                                item.getPrice()
                        )).collect(Collectors.toList()),
                order.getTotalPrice(),
                order.getOrderDate(),
                order.getStatus().name()
        );
    }

    @Override
    public OrderListingDto updateOrderState(Integer id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new BusinessException("Order not found"));
        OrderStatus currentStatus = order.getStatus();
        handleOrderStatusTransition(currentStatus, order);
        orderRepository.save(order);

        return new OrderListingDto(
                order.getId(),
                order.getUser().getId(),
                order.getOrderItems().stream().map(
                        item -> new OrderItemDto(
                                item.getId(),
                                item.getProduct().getId(),
                                item.getQuantity(),
                                item.getPrice()
                        )).collect(Collectors.toList()),
                order.getTotalPrice(),
                order.getOrderDate(),
                order.getStatus().name()
        );
    }

    @Override
    public List<OrderListingDto> getAllUserOrders(UUID userId) {

        List<Order> orders = orderRepository.findByUserIdOrderByOrderDateDesc(userId);

        return orders.stream().map(order -> new OrderListingDto(
                order.getId(),
                order.getUser().getId(),
                order.getOrderItems().stream().map(
                        item -> new OrderItemDto(
                                item.getId(),
                                item.getProduct().getId(),
                                item.getQuantity(),
                                item.getPrice()
                        )).collect(Collectors.toList()),
                order.getTotalPrice(),
                order.getOrderDate(),
                order.getStatus().name()
        )).collect(Collectors.toList());
    }

    private void handleOrderStatusTransition(OrderStatus currentStatus, Order order) {

        if (currentStatus == OrderStatus.HAZIRLANIYOR) {
            order.setStatus(OrderStatus.KARGODA);
        } else if (currentStatus == OrderStatus.KARGODA) {
            order.setStatus(OrderStatus.TESLIM_EDILDI);
        } else if (currentStatus == OrderStatus.TESLIM_EDILDI) {
            throw new BusinessException("Order already delivered. No further status updates allowed.");
        } else {
            throw new BusinessException("Invalid order status transition.");
        }
    }
}
