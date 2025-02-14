package zw.co.trolley.PaymentService.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import zw.co.paynow.constants.MobileMoneyMethod;
import zw.co.paynow.core.Payment;
import zw.co.paynow.core.Paynow;
import zw.co.paynow.responses.MobileInitResponse;
import zw.co.paynow.responses.StatusResponse;
import zw.co.paynow.responses.WebInitResponse;
import zw.co.trolley.PaymentService.dtos.CheckPaymentResponseDTO;
import zw.co.trolley.PaymentService.dtos.PaymentDto;
import zw.co.trolley.PaymentService.dtos.PaymentResponse;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaynowService {
    private final Paynow paynow;
    private final RestTemplate restTemplate;

    public PaynowService(Paynow paynow, RestTemplate restTemplate) {
        this.paynow = paynow;
        this.restTemplate = restTemplate;
    }

    public PaymentResponse createPayment(PaymentDto paymentDto) {
        Payment payment = paynow.createPayment(paymentDto.getOrderId());
        paymentDto.getOrderItems().forEach(orderItemDto -> payment.add(orderItemDto.getProductId(), orderItemDto.getPrice().multiply(orderItemDto.getPrice())));
        payment.setCartDescription("Some custom description");//this is optional
        paynow.setReturnUrl("http://localhost:5173/order/"+paymentDto.getOrderId());
        // Save the response from paynow in a variable
        WebInitResponse response = paynow.send(payment);
        if (response.success()) {
            return PaymentResponse.builder().pollUrl(response.pollUrl()).redirectUrl(response.getRedirectURL()).build();
        }
        throw new RuntimeException("*freak out*");
    }

    public PaymentResponse initiateMobilePayments(PaymentDto paymentDto) {
        Payment payment = paynow.createPayment(paymentDto.getOrderId(), "tendaikatsande@live.com");
        paymentDto.getOrderItems().forEach(orderItemDto -> payment.add(orderItemDto.getProductId(), orderItemDto.getPrice().multiply(orderItemDto.getPrice())));
        MobileInitResponse response = paynow.sendMobile(payment, paymentDto.getPhoneNumber(), MobileMoneyMethod.valueOf(paymentDto.getMobileMoneyMethod()));
        if (response.success()) {
            return PaymentResponse.builder().pollUrl(response.pollUrl()).redirectUrl(response.getInstructions()).build();
        }
        throw new RuntimeException("*freak out*");
    }

    public Boolean checkIsPaid(String pollUrl) {
        // Check the status of the transaction with the specified pollUrl
        StatusResponse status = paynow.pollTransaction(pollUrl);
        return status.isPaid();
    }

    public CheckPaymentResponseDTO checkPayment(String url) {
        // Make the GET request
        String response = restTemplate.getForObject(url, String.class);
        // Convert URL-encoded response to a Map
        Map<String, String> responseMap = Arrays.stream(response.split("&"))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(pair -> pair[0], pair -> pair.length > 1 ? pair[1] : ""));
        // Map response to DTO
        CheckPaymentResponseDTO dto = new CheckPaymentResponseDTO();
        dto.setReference(responseMap.get("reference"));
        dto.setPayNowReference(responseMap.get("paynowreference"));
        dto.setAmount(Double.parseDouble(responseMap.get("amount")));
        dto.setStatus(responseMap.get("status"));
        dto.setPollUrl(responseMap.get("pollurl"));
        dto.setHash(responseMap.get("hash"));
        return dto;
    }
}
