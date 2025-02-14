package zw.co.trolley.PaymentService.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CheckPaymentResponseDTO {

    private String reference;

    @JsonProperty("paynowreference")
    private String payNowReference;

    private double amount;
    private String status;

    @JsonProperty("pollurl")
    private String pollUrl;

    private String hash;


}
