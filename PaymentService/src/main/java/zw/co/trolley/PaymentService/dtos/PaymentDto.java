package zw.co.trolley.PaymentService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private String orderId;
    private String phoneNumber;
    private String mobileMoneyMethod;
    private List<OrderItemDto> orderItems;
}


