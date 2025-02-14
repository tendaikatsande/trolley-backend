package zw.co.trolley.PaymentService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private String productId;
    private Integer quantity;
    private BigDecimal price;
}
