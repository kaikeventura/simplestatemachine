package com.kaikeventura.simplestatemachine.config;

import java.util.EnumSet;
import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

import com.kaikeventura.simplestatemachine.model.PaymentEvent;
import com.kaikeventura.simplestatemachine.model.PaymentState;

@Configuration
@EnableStateMachineFactory
public class PaymentStateMachineConfig extends StateMachineConfigurerAdapter<PaymentState, PaymentEvent> {
    
    @Override
    public void configure(StateMachineStateConfigurer<PaymentState, PaymentEvent> states) throws Exception {
        states.withStates()
            .initial(PaymentState.CREATED)
            .states(EnumSet.allOf(PaymentState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<PaymentState, PaymentEvent> transitions) throws Exception {
        transitions
            .withExternal().source(PaymentState.CREATED).target(PaymentState.CREATED)
                .event(PaymentEvent.CHECK_APPROVE_PAYMENT).action(checkApproved())
            .and()
            .withExternal().source(PaymentState.CREATED).target(PaymentState.APPROVED)
                .event(PaymentEvent.APPROVE_PAYMENT).action(checkApproved())
            .and()
            .withExternal().source(PaymentState.CREATED).target(PaymentState.NO_APPROVED)
                .event(PaymentEvent.NO_APPROVE_PAYMENT)
            .and()
            
            .withExternal().source(PaymentState.APPROVED).target(PaymentState.APPROVED)
                .event(PaymentEvent.CHECK_COMPLETE_PAYMENT).action(checkCompletePayment())
            .and()
            .withExternal().source(PaymentState.NO_APPROVED).target(PaymentState.NO_APPROVED)
                .event(PaymentEvent.CHECK_COMPLETE_PAYMENT).action(checkCompletePayment())
            .and()
            .withExternal().source(PaymentState.APPROVED).target(PaymentState.COMPLETE)
                .event(PaymentEvent.COMPLETE_PAYMENT);
    }

    @Bean
    public StateMachineService<PaymentState, PaymentEvent> stateMachineService(
        StateMachineFactory<PaymentState, PaymentEvent> stateMachineFactory
    ) {
        return new DefaultStateMachineService<>(stateMachineFactory);
    }

    private Action<PaymentState, PaymentEvent> checkApproved() {
        return context -> {
            if (new Random().nextInt(10) < 7) {
                System.out.println("Approved");
                context.getStateMachine().sendEvent(PaymentEvent.APPROVE_PAYMENT);
            }
            else {
                System.out.println("No Approved");
                context.getStateMachine().sendEvent(PaymentEvent.NO_APPROVE_PAYMENT);
            }
        };
    }

    private Action<PaymentState, PaymentEvent> checkCompletePayment() {
        return context -> {
            if (context.getStateMachine().getState().getId() == PaymentState.APPROVED) {
                System.out.println("Complete");
                context.getStateMachine().sendEvent(PaymentEvent.COMPLETE_PAYMENT);
            }
            else {
                System.out.println("No Complete");
                context.getStateMachine().sendEvent(PaymentEvent.NO_APPROVE_PAYMENT);
            }
        };
    }

}
