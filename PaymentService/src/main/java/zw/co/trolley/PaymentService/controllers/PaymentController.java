package zw.co.trolley.PaymentService.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.trolley.PaymentService.dtos.CheckPaymentResponseDTO;
import zw.co.trolley.PaymentService.dtos.CheckTransactionRequest;
import zw.co.trolley.PaymentService.dtos.PaymentDto;
import zw.co.trolley.PaymentService.dtos.PaymentResponse;
import zw.co.trolley.PaymentService.services.PaynowService;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaynowService paynowService;

    @PostMapping("/web")
    public ResponseEntity<PaymentResponse> initiateWebPayment(@RequestBody PaymentDto paymentDto) {
        return ResponseEntity.ok(paynowService.createPayment(paymentDto)) ;
    }

    @PostMapping("/mobile")
    public ResponseEntity<PaymentResponse> initiateMobilePayment(@RequestBody PaymentDto paymentDto) {
        return ResponseEntity.ok( paynowService.initiateMobilePayments(paymentDto));
    }

    @PostMapping("/poll")
    public ResponseEntity<Boolean> checkIsPaid(@RequestBody CheckTransactionRequest checkTransactionRequest) {
        return ResponseEntity.ok(paynowService.checkIsPaid(checkTransactionRequest.getUrl()));
    }

    @PostMapping("/check")
    public ResponseEntity<CheckPaymentResponseDTO> checkPayment(@RequestBody CheckTransactionRequest checkTransactionRequest) {
        return ResponseEntity.ok(paynowService.checkPayment(checkTransactionRequest.getUrl()));
    }
}
