package com.turkcell.mini_e_commere_hw2.service;

import com.turkcell.mini_e_commere_hw2.dto.order.OrderItemDto;
import com.turkcell.mini_e_commere_hw2.dto.order.OrderListingDto;
import com.turkcell.mini_e_commere_hw2.entity.Order;
import com.turkcell.mini_e_commere_hw2.entity.OrderItem;
import com.turkcell.mini_e_commere_hw2.entity.OrderStatus;
import com.turkcell.mini_e_commere_hw2.entity.User;
import com.turkcell.mini_e_commere_hw2.repository.OrderItemRepository;
import com.turkcell.mini_e_commere_hw2.repository.OrderRepository;
import com.turkcell.mini_e_commere_hw2.rules.OrderBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderBusinessRules orderBusinessRules;
    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderBusinessRules orderBusinessRules,
                            ProductService productService,
                            CartService cartService,
                            UserService userService,
                            OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderBusinessRules = orderBusinessRules;
        this.productService = productService;
        this.userService = userService;
        this.cartService = cartService;
    }

    @Override
    public void createOrder(UUID userId) {
        User user = userService.findById(userId);
        orderBusinessRules.cartMustNotBeEmpty(user.getCart());
        orderBusinessRules.checkTheProductStockAfterUpdateProductStockForOrder(user.getCart(),productService);

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.HAZIRLANIYOR);
        order.setTotalPrice(user.getCart().getTotalPrice());


        List<OrderItem> orderItems = user.getCart().getCartItems().stream().map(item ->{
                    OrderItem orderItem = new OrderItem();
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setPrice(item.getProduct().getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                    orderItem.setOrder(order);
                    return orderItem;
                }).collect(Collectors.toList());

        order.setOrderItems(orderItems);

        user.getCart().getCartItems().clear();  // sepetteki ürünleri sil

        cartService.saveCard(user.getCart());
        orderRepository.save(order);
    }

    @Override
    public void updateOrderState(Integer id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new BusinessException("Order not found"));
        OrderStatus currentStatus = order.getStatus();
        orderBusinessRules.handleOrderStatusTransition(currentStatus,order);
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
                order.getTotalPrice()
        )).collect(Collectors.toList());


    }
}
