package zw.co.trolley.OrderService.domain.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {
    private String pollUrl;
    private String redirectUrl;
    private String instructions;
}
