package zw.co.trolley.OrderService.domain.models;

public enum OrderStatus {
    PENDING,
    WAITING_FOR_PAYMENT,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
