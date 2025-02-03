package com.turkcell.mini_e_commere_hw2.application.order.commands.delete;

import an.awesome.pipelinr.Command;
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
public class DeleteOrderCommand implements Command<Void> {
    @NotNull(message = "Order ID cannot be null")
    private Integer id;

    @Component
    @RequiredArgsConstructor
    public static class Handler implements Command.Handler<DeleteOrderCommand, Void> {
        private final OrderRepository orderRepository;

        @Override
        public Void handle(DeleteOrderCommand command) {
            Order order = orderRepository.findById(command.getId())
                    .orElseThrow(() -> new BusinessException("Order not found"));

            orderRepository.delete(order);
            return null;
        }
    }
} 