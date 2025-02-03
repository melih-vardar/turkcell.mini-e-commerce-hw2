package com.turkcell.mini_e_commere_hw2.application.order.commands.create;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.dto.order.OrderItemDto;
import com.turkcell.mini_e_commere_hw2.entity.Cart;
import com.turkcell.mini_e_commere_hw2.entity.Order;
import com.turkcell.mini_e_commere_hw2.entity.OrderItem;
import com.turkcell.mini_e_commere_hw2.entity.User;
import com.turkcell.mini_e_commere_hw2.enums.OrderStatus;
import com.turkcell.mini_e_commere_hw2.repository.OrderRepository;
import com.turkcell.mini_e_commere_hw2.repository.UserRepository;
import com.turkcell.mini_e_commere_hw2.rules.OrderBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import lombok.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderCommand implements Command<CreatedOrderResponse> {
    private UUID userId; // Optional, if not provided, use authenticated user

    @Component
    @RequiredArgsConstructor
    public static class Handler implements Command.Handler<CreateOrderCommand, CreatedOrderResponse> {
        private final OrderRepository orderRepository;
        private final UserRepository userRepository;
        private final OrderBusinessRules orderBusinessRules;

        @Override
        public CreatedOrderResponse handle(CreateOrderCommand command) {

            UUID userId;

            if(command.getUserId() == null) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication == null || !authentication.isAuthenticated()) {
                throw new BusinessException("No authenticated user found");
            }

            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new BusinessException("User not found"));


                userId = user.getId();
            }
            else {
                userId = command.getUserId();
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new BusinessException("User not found"));

            Cart cart = user.getCart();
            orderBusinessRules.cartMustNotBeEmpty(cart);
            orderBusinessRules.checkTheProductStockAfterUpdateProductStockForOrder(cart);

            Order order = new Order();
            order.setUser(user);
            order.setOrderDate(LocalDateTime.now());
            order.setStatus(OrderStatus.PREPARING);
            order.setTotalPrice(cart.getTotalPrice());

            List<OrderItem> orderItems = cart.getCartItems().stream()
                    .map(cartItem -> {
                        OrderItem orderItem = new OrderItem();
                        orderItem.setProduct(cartItem.getProduct());
                        orderItem.setQuantity(cartItem.getQuantity());
                        orderItem.setPrice(cartItem.getProduct().getUnitPrice()
                                .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                        orderItem.setOrder(order);
                        return orderItem;
                    }).collect(Collectors.toList());

            order.setOrderItems(orderItems);
            orderRepository.save(order);

            return new CreatedOrderResponse(
                    order.getId(),
                    order.getUser().getId(),
                    order.getOrderItems().stream()
                            .map(orderItem -> new OrderItemDto(
                                    orderItem.getId(),
                                    orderItem.getProduct().getId(),
                                    orderItem.getQuantity(),
                                    orderItem.getPrice()))
                            .collect(Collectors.toList()),
                    order.getTotalPrice(),
                    order.getOrderDate(),
                    order.getStatus().name());
        }
    }
} 