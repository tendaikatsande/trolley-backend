package zw.co.trolley.OrderService.domain.dtos;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import zw.co.trolley.OrderService.utils.JacksonLocalDateTimeSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderDto {
    private UUID id;
    private UUID userId;
    private String status;
    private BigDecimal totalAmount;
    @JsonSerialize(using = JacksonLocalDateTimeSerializer.class)
    private LocalDateTime createdAt;
    private List<OrderItemDto> items;
    private ShippingAddressDto shippingAddress;
    private String paymentMethod;
    private String paymentStatus;
    private String paymentReference;
    private String pollUrl;
    private String redirectUrl;
    private LocalDateTime paymentDate;
}


