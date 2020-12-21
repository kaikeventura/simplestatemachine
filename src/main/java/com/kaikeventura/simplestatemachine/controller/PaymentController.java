package com.kaikeventura.simplestatemachine.controller;

import com.kaikeventura.simplestatemachine.model.Payment;
import com.kaikeventura.simplestatemachine.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Payment> create() {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment());
    }

    @PatchMapping("{id}/approve")
    public ResponseEntity<Payment> approve(@PathVariable("id") final Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.approvedPayment(id));
    }

    @PatchMapping("{id}/complete")
    public ResponseEntity<Payment> complete(@PathVariable("id") final Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.completePayment(id));
    }

    @GetMapping("{id}")
    public ResponseEntity<Payment> findPayment(@PathVariable("id") final Long id) {
        return paymentService.findPaymentById(id).map(payment -> ResponseEntity.status(HttpStatus.OK).body(payment))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
