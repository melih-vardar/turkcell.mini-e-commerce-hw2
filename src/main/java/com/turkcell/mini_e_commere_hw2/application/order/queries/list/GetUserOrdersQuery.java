package com.turkcell.mini_e_commere_hw2.application.order.queries.list;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.dto.order.OrderListingDto;
import com.turkcell.mini_e_commere_hw2.entity.Order;
import com.turkcell.mini_e_commere_hw2.entity.User;
import com.turkcell.mini_e_commere_hw2.repository.OrderRepository;
import com.turkcell.mini_e_commere_hw2.repository.UserRepository;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import lombok.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserOrdersQuery implements Command<List<OrderListingDto>> {
    private UUID userId;

    @Component
    @RequiredArgsConstructor
    public static class Handler implements Command.Handler<GetUserOrdersQuery, List<OrderListingDto>> {
        private final OrderRepository orderRepository;
        private final UserRepository userRepository;

        @Override
        public List<OrderListingDto> handle(GetUserOrdersQuery query) {

            UUID userId;

            if(query.getUserId() == null) {

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
                userId = query.getUserId();
            }

            List<Order> orders = orderRepository.findByUserIdOrderByOrderDateDesc(userId);
            return orders.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
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