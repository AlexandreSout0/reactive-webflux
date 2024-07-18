package com.alx.reactive_webflux.model;

public class payment {
    String id;
    String userId;
    PaymentStatus status;

    public enum  PaymentStatus {
        PENDING,
        APPROVED
    }
}
