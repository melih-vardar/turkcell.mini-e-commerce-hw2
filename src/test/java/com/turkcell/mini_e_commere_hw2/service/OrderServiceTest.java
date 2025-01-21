package com.turkcell.mini_e_commere_hw2.service;

import com.turkcell.mini_e_commere_hw2.dto.order.OrderListingDto;
import com.turkcell.mini_e_commere_hw2.entity.*;
import com.turkcell.mini_e_commere_hw2.enums.OrderStatus;
import com.turkcell.mini_e_commere_hw2.repository.OrderItemRepository;
import com.turkcell.mini_e_commere_hw2.repository.OrderRepository;
import com.turkcell.mini_e_commere_hw2.rules.OrderBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderBusinessRules orderBusinessRules;
    @Mock
    private ProductService productService;
    @Mock
    private UserService userService;
    @Mock
    private CartService cartService;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderRepository, orderBusinessRules, 
                productService, userService, cartService);
    }

    @Test
    void createOrder_WithEmptyCart_ShouldThrowException() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>());
        user.setCart(cart);

        when(userService.findById(userId)).thenReturn(user);
        doThrow(new BusinessException("Cart is empty. Cannot create order."))
            .when(orderBusinessRules).cartMustNotBeEmpty(cart);

        // Act & Assert
        assertThrows(BusinessException.class, () -> orderService.createOrder(userId));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_WithInsufficientStock_ShouldThrowException() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        Cart cart = new Cart();
        cart.setCartItems(Arrays.asList(new CartItem())); // Non-empty cart
        user.setCart(cart);

        when(userService.findById(userId)).thenReturn(user);
        doThrow(new BusinessException("Insufficient stock"))
            .when(orderBusinessRules).checkTheProductStockAfterUpdateProductStockForOrder(eq(cart), any());

        // Act & Assert
        assertThrows(BusinessException.class, () -> orderService.createOrder(userId));
        verify(orderRepository, never()).save(any());
    }


    @Test
    void updateOrderState_WithInvalidId_ShouldThrowException() {
        // Arrange
        Integer orderId = 999;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> orderService.updateOrderState(orderId));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getAllUserOrders_ShouldReturnUserOrders() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        Order order1 = new Order();
        order1.setId(1);
        order1.setUser(user);
        order1.setTotalPrice(BigDecimal.valueOf(99.99));
        order1.setOrderDate(LocalDateTime.now().minusDays(1));
        order1.setOrderItems(new ArrayList<>());

        Order order2 = new Order();
        order2.setId(2);
        order2.setUser(user);
        order2.setTotalPrice(BigDecimal.valueOf(149.99));
        order2.setOrderDate(LocalDateTime.now());
        order2.setOrderItems(new ArrayList<>());

        when(orderRepository.findByUserIdOrderByOrderDateDesc(userId))
            .thenReturn(Arrays.asList(order2, order1));

        // Act
        List<OrderListingDto> result = orderService.getAllUserOrders(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(order2.getId(), result.get(0).getId());
        assertEquals(order1.getId(), result.get(1).getId());
        assertEquals(userId, result.get(0).getUserId());
        assertEquals(userId, result.get(1).getUserId());
    }

    @Test
    void getAllUserOrders_WithInvalidUserId_ShouldReturnEmptyList() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(orderRepository.findByUserIdOrderByOrderDateDesc(userId))
            .thenReturn(new ArrayList<>());

        // Act
        List<OrderListingDto> result = orderService.getAllUserOrders(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
} 