package com.alx.reactive_webflux.repository;

import com.alx.reactive_webflux.model.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentRepository {

    private final Database database;

//    static {
//        Schedulers.parallel();
//        Schedulers.boundedElastic();
//        Schedulers.immediate();
//        Schedulers.single();
//    }

    public Mono<Payment> createPayment(final String userId) {
        final Payment payment = Payment.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .status(Payment.PaymentStatus.PENDING)
                .build();

        return Mono.fromCallable( () -> {
            log.info("Saving payment transaction for user {}", userId);
            return this.database.save(userId,payment);
        })
        .subscribeOn(Schedulers.boundedElastic())
        .doOnNext( next -> log.info("Payment received {}", payment.getUserId()));
    }

    public Mono<Payment> getPayment(final String userId){
                    log.info("Getting payment from database - {}", userId);
            final Optional<Payment> payment = this.database.get(userId, Payment.class);
            return Mono.justOrEmpty(payment);
        })
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(it -> log.info("Payment received - {}",userId));
    }

}
