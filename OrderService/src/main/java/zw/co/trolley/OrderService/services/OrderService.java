package zw.co.trolley.OrderService.services;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.trolley.OrderService.domain.dtos.OrderDto;
import zw.co.trolley.OrderService.domain.dtos.OrderItemDto;
import zw.co.trolley.OrderService.domain.dtos.ShippingAddressDto;
import zw.co.trolley.OrderService.domain.models.Order;
import zw.co.trolley.OrderService.domain.models.OrderItem;
import zw.co.trolley.OrderService.domain.models.OrderStatus;
import zw.co.trolley.OrderService.domain.models.ShippingAddress;
import zw.co.trolley.OrderService.domain.repositories.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setUserId(orderDto.getUserId());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(orderDto.getTotalAmount());
        order.setCreatedAt(LocalDateTime.now());

        // Set shipping address
        ShippingAddress shippingAddress = mapToShippingAddress(orderDto.getShippingAddress());
        shippingAddress.setOrder(order);
        order.setShippingAddress(shippingAddress);

        // Set order items
        Order finalOrder = order;
        List<OrderItem> items = orderDto.getItems().stream()
                .map(itemDto -> {
                    OrderItem item = new OrderItem();
                    item.setOrder(finalOrder);
                    item.setProductId(itemDto.getProductId());
                    item.setProductName(itemDto.getProductName());
                    item.setQuantity(itemDto.getQuantity());
                    item.setPrice(itemDto.getPrice());
                    return item;
                })
                .collect(Collectors.toList());
        order.setItems(items);

        order = orderRepository.save(order);

        // Send notification event
        rabbitTemplate.convertAndSend("order-exchange", "order.created", order.getId());

        return mapToDto(order);
    }

    public List<OrderDto> getUserOrders(UUID userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public OrderDto getOrder(UUID userId, UUID orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapToDto(order);
    }

    @Transactional
    public OrderDto cancelOrder(UUID userId, UUID orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Order cannot be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order = orderRepository.save(order);

        // Send notification event
        rabbitTemplate.convertAndSend("order-exchange", "order.cancelled", order.getId());

        return mapToDto(order);
    }

    // Mapping methods...
    private OrderDto mapToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setStatus(order.getStatus().name());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setItems(order.getItems().stream()
                .map(this::mapToItemDto)
                .collect(Collectors.toList()));
        dto.setShippingAddress(mapToShippingAddressDto(order.getShippingAddress()));
        return dto;
    }

    private OrderItemDto mapToItemDto(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProductId());
        dto.setProductName(item.getProductName());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        return dto;
    }

    private ShippingAddressDto mapToShippingAddressDto(ShippingAddress address) {
        ShippingAddressDto dto = new ShippingAddressDto();
        dto.setId(address.getId());
        dto.setFullName(address.getFullName());
        dto.setAddressLine1(address.getAddressLine1());
        dto.setAddressLine2(address.getAddressLine2());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPostalCode(address.getPostalCode());
        dto.setCountry(address.getCountry());
        dto.setPhone(address.getPhone());
        return dto;
    }

    private ShippingAddress mapToShippingAddress(ShippingAddressDto dto) {
        ShippingAddress address = new ShippingAddress();
        address.setFullName(dto.getFullName());
        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());
        address.setPhone(dto.getPhone());
        return address;
    }
}
