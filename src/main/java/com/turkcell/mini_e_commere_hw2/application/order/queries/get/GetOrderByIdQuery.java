package com.turkcell.mini_e_commere_hw2.application.order.queries.get;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.dto.order.OrderListingDto;
import com.turkcell.mini_e_commere_hw2.entity.Order;
import com.turkcell.mini_e_commere_hw2.repository.OrderRepository;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
public class GetOrderByIdQuery implements Command<OrderListingDto> {
    @NotNull(message = "Order ID cannot be null")
    private Integer id;

    @Component
    @RequiredArgsConstructor
    public static class Handler implements Command.Handler<GetOrderByIdQuery, OrderListingDto> {
        private final OrderRepository orderRepository;

        @Override
        public OrderListingDto handle(GetOrderByIdQuery query) {
            Order order = orderRepository.findById(query.getId())
                    .orElseThrow(() -> new BusinessException("Order not found"));

            return mapToDto(order);
        }

        private OrderListingDto mapToDto(Order order) {
            OrderListingDto dto = new OrderListingDto();
            dto.setId(order.getId());
            dto.setUserId(order.getUser().getId());
            dto.setOrderDate(order.getOrderDate());
            dto.setStatus(order.getStatus().name());
            dto.setTotalPrice(order.getTotalPrice());
            return dto;
        }
    }
} 