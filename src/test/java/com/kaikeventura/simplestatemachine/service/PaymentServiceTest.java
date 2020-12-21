package com.kaikeventura.simplestatemachine.service;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PaymentServiceTest {
    
    @Autowired
    private PaymentService paymentService;

    @Test
    void runTest() {
        val paymentCreated = paymentService.createPayment();
        System.out.println(paymentCreated.getPaymentState());

        val paymentApproved = paymentService.approvedPayment(paymentCreated.getId());
        System.out.println(paymentApproved.getPaymentState());

        val paymentComplete = paymentService.completePayment(paymentApproved.getId());
        System.out.println(paymentComplete.getPaymentState());
    }
}
