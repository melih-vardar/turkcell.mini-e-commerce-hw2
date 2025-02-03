package com.turkcell.mini_e_commere_hw2.application.order.commands.update;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.dto.order.OrderItemDto;
import com.turkcell.mini_e_commere_hw2.dto.order.OrderListingDto;
import com.turkcell.mini_e_commere_hw2.entity.Order;
import com.turkcell.mini_e_commere_hw2.enums.OrderStatus;
import com.turkcell.mini_e_commere_hw2.repository.OrderRepository;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class UpdateOrderStatusCommand implements Command<OrderListingDto> {
    @NotNull(message = "Order ID cannot be null")
    private Integer orderId;

    private static final LinkedList<OrderStatus> ORDER_STATUS_PROGRESSION = new LinkedList<>();

    static {
        ORDER_STATUS_PROGRESSION.add(OrderStatus.PREPARING);
        ORDER_STATUS_PROGRESSION.add(OrderStatus.SHIPPED);
        ORDER_STATUS_PROGRESSION.add(OrderStatus.DELIVERED);
    }

    @Component
    @RequiredArgsConstructor
    public static class Handler implements Command.Handler<UpdateOrderStatusCommand, OrderListingDto> {
        private final OrderRepository orderRepository;

        @Override
        public OrderListingDto handle(UpdateOrderStatusCommand command) {
            Order order = orderRepository.findById(command.getOrderId())
                    .orElseThrow(() -> new BusinessException("Order not found"));

            OrderStatus currentStatus = order.getStatus();
            handleOrderStatusTransition(currentStatus, order);
            orderRepository.save(order);

            return new OrderListingDto(
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
} 