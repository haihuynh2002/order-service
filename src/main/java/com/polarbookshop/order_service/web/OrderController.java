package com.polarbookshop.order_service.web;

import com.polarbookshop.order_service.domain.Order;
import com.polarbookshop.order_service.domain.OrderService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Flux<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping
    public Mono<Order> submitOrder(
            @RequestBody OrderRequest request
    ) {
        return orderService.submitOrder(
                request.isbn(),
                request.quantity()
        );
    }
}
