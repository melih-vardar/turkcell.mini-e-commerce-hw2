package com.turkcell.mini_e_commere_hw2.controller;

import an.awesome.pipelinr.Pipeline;
import com.turkcell.mini_e_commere_hw2.application.order.commands.create.CreateOrderCommand;
import com.turkcell.mini_e_commere_hw2.application.order.commands.create.CreatedOrderResponse;
import com.turkcell.mini_e_commere_hw2.application.order.commands.delete.DeleteOrderCommand;
import com.turkcell.mini_e_commere_hw2.application.order.commands.update.UpdateOrderStatusCommand;
import com.turkcell.mini_e_commere_hw2.application.order.queries.get.GetOrderByIdQuery;
import com.turkcell.mini_e_commere_hw2.application.order.queries.list.GetUserOrdersQuery;
import com.turkcell.mini_e_commere_hw2.core.web.BaseController;
import com.turkcell.mini_e_commere_hw2.dto.order.OrderListingDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController extends BaseController {

    public OrderController(Pipeline pipeline) {
        super(pipeline);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedOrderResponse createOrder() {
        return pipeline.send(new CreateOrderCommand());
    }

    @PostMapping("/admin/create-for-user/{userId}")
    @PreAuthorize("hasAuthority('admin')")
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedOrderResponse createOrderForUser(@PathVariable UUID userId) {
        return pipeline.send(new CreateOrderCommand(userId));
    }

    @PutMapping("/{orderId}/status")
    public void updateOrderStatus(@PathVariable Integer orderId) {
        pipeline.send(new UpdateOrderStatusCommand(orderId));
    }

    @GetMapping
    public List<OrderListingDto> getAllUserOrders() {
        return pipeline.send(new GetUserOrdersQuery());
    }

    @GetMapping("/admin/user/{userId}")
    @PreAuthorize("hasAuthority('admin')")
    public List<OrderListingDto> getUserOrders(@PathVariable UUID userId) {
        return pipeline.send(new GetUserOrdersQuery(userId));
    }

    @GetMapping("/{orderId}")
    public OrderListingDto getOrderById(@PathVariable Integer orderId) {
        return pipeline.send(new GetOrderByIdQuery(orderId));
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Integer orderId) {
        pipeline.send(new DeleteOrderCommand(orderId));
    }
}