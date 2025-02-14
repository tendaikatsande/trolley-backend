package zw.co.trolley.OrderService.domain.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private ShippingAddress shippingAddress;

    private String paymentMethod;

    private String paymentStatus;

    private String paymentReference;

    private String pollUrl;

    private String redirectUrl;

    private LocalDateTime paymentDate;


}
