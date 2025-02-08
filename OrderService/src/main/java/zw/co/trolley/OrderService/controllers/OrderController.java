package zw.co.trolley.OrderService.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.trolley.OrderService.domain.dtos.OrderDto;
import zw.co.trolley.OrderService.services.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(
            @RequestHeader("X-User-ID") UUID userId,
            @RequestBody OrderDto orderDto) {
        orderDto.setUserId(userId);
        return ResponseEntity.ok(orderService.createOrder(orderDto));
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getUserOrders(
            @RequestHeader("X-User-ID") UUID userId) {
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(
            @RequestHeader("X-User-ID") UUID userId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrder(userId, id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderDto> cancelOrder(
            @RequestHeader("X-User-ID") UUID userId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(orderService.cancelOrder(userId, id));
    }
}
