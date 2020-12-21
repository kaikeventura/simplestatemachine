package com.kaikeventura.simplestatemachine.model;

public enum PaymentState {
    CREATED,
    APPROVED,
    COMPLETE,
    NO_APPROVED,
    PENDING_CANCELLATION,
    APPROVED_CANCELLATION,
    COMPLETE_CANCELLATION,
    NO_APPROVED_CANCELLATION
}
