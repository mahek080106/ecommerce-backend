package ecommerce.task.controller;

import ecommerce.task.model.Order;
import ecommerce.task.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(
            Authentication authentication) {

        String userEmail = authentication.getName();

        return ResponseEntity.ok(
                orderService.placeOrder(userEmail)
        );
    }

    @GetMapping
    public ResponseEntity<List<Order>> getMyOrders(
            Authentication authentication) {

        String userEmail = authentication.getName();

        return ResponseEntity.ok(
                orderService.getMyOrders(userEmail)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(
            @PathVariable String id,
            Authentication authentication) {

        String userEmail = authentication.getName();

        return ResponseEntity.ok(
                orderService.getOrderById(
                        id,
                        userEmail
                )
        );
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable String id,
            Authentication authentication) {

        String userEmail = authentication.getName();

        return ResponseEntity.ok(
                orderService.cancelOrder(
                        id,
                        userEmail
                )
        );
    }
}