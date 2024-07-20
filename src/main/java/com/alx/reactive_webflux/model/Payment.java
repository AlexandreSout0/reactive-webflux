package com.alx.reactive_webflux.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Payment {
    String id;
    String userId;
    PaymentStatus status;

    public enum  PaymentStatus {
        PENDING,
        APPROVED
    }
}
