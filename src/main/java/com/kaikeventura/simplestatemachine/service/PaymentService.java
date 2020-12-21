package com.kaikeventura.simplestatemachine.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import com.kaikeventura.simplestatemachine.model.Payment;
import com.kaikeventura.simplestatemachine.model.PaymentEvent;
import com.kaikeventura.simplestatemachine.model.PaymentState;
import com.kaikeventura.simplestatemachine.repository.PaymentRepository;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class PaymentService {
    
    private final StateMachineService<PaymentState, PaymentEvent> stateMachineService;
    private final PaymentRepository paymentRepository;

    public Payment createPayment() {
        val uuid = UUID.randomUUID();
        stateMachineService.acquireStateMachine(uuid.toString());

        return paymentRepository.save(
            Payment.builder()
                .amount(BigDecimal.valueOf(new Random().nextDouble()))
                .paymentState(PaymentState.CREATED)
                .machineId(uuid.toString())
                .build()
        );
    }

    public Payment approvedPayment(final Long PaymentId) {
        val payment = paymentRepository.findById(PaymentId).orElseThrow(RuntimeException::new);
        val sm = stateMachineService.acquireStateMachine(payment.getMachineId());

        sendEvent(PaymentEvent.CHECK_APPROVE_PAYMENT, sm);
        payment.setPaymentState(sm.getState().getId());

        return paymentRepository.save(payment);
    }

    public Payment completePayment(final Long PaymentId) {
        val payment = paymentRepository.findById(PaymentId).orElseThrow(RuntimeException::new);
        val sm = stateMachineService.acquireStateMachine(payment.getMachineId());

        sendEvent(PaymentEvent.CHECK_COMPLETE_PAYMENT, sm);
        payment.setPaymentState(sm.getState().getId());

        return paymentRepository.save(payment);
    }

    public Optional<Payment> findPaymentById(final Long id) {
        return paymentRepository.findById(id);
    }

    private void sendEvent(PaymentEvent paymentEvent, StateMachine<PaymentState, PaymentEvent> sm) {
        sm.sendEvent(paymentEvent);
    }

}
