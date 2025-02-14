package zw.co.trolley.OrderService.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.trolley.OrderService.domain.dtos.CheckPaymentResponseDTO;
import zw.co.trolley.OrderService.domain.dtos.OrderDto;
import zw.co.trolley.OrderService.domain.dtos.PaymentResponse;
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
    public ResponseEntity<Page<OrderDto>> getUserOrders(
            @RequestHeader("X-User-ID") UUID userId, Pageable pageable) {
        return ResponseEntity.ok(orderService.getUserOrders(userId, pageable));
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

    @PutMapping("/{id}/payment")
    public ResponseEntity<OrderDto> createOrderPaymentRequest(
            @RequestHeader("X-User-ID") UUID userId,
            @PathVariable UUID id,
            @RequestBody PaymentResponse paymentResponse) {
        return ResponseEntity.ok(orderService.createOrderPaymentRequest(userId, id, paymentResponse));
    }

    @PutMapping("/{id}/payment/update")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @RequestHeader("X-User-ID") UUID userId,
            @PathVariable UUID id,
            @RequestBody CheckPaymentResponseDTO checkPaymentResponseDTO) {
        return ResponseEntity.ok(orderService.updateOrderStatus(userId, id, checkPaymentResponseDTO));
    }
}
