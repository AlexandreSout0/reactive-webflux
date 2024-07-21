package com.alx.reactive_webflux.controller;


import com.alx.reactive_webflux.model.Payment;
import com.alx.reactive_webflux.publishers.PaymentPublisher;
import com.alx.reactive_webflux.repository.PaymentRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@RestController
@RequestMapping(value = "payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentRepository paymentRepository;
    private final PaymentPublisher paymentPublisher;
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Payment> createPayment(@RequestBody final NewPaymentInput input){
        final String userId = input.getUserId();
        log.info("Payment to be processed {}", userId);
        return this.paymentRepository.createPayment(userId)
                .flatMap(payment -> this.paymentPublisher.onPaymentCreate(payment))
                .flatMap(payment ->
                        {
                            Mono<Payment> mono = Flux.interval(Duration.ofSeconds(1))
                                    .doOnNext(it -> log.info("Next Tick {}",it))
                                    .flatMap(tick -> this.paymentRepository.getPayment(userId))
                                    .filter(it -> Payment.PaymentStatus.APPROVED == it.getStatus())
                                    .next();
                            return mono;
                        }
                )
                .doOnNext(next -> log.info("Payment Processed {} ", userId))
                        .timeout(Duration.ofSeconds(5))
                        .retry(3)
                        .retryWhen(Retry.backoff(2, Duration.ofSeconds(1))
                                .doAfterRetry(signal -> log.info("Execution failed ... retrying...{}",signal))
                        );
    }


    @Data
    public static class NewPaymentInput {
        private String userId;
    }

}



//                .flatMap( payment -> this.paymentRepository.createPayment(t))
//        .flatMap( payment -> t.commit())
//        .onErrorContinue( RuntimeException.class, throwable -> {
//        return  Payment.builder().build();
//                })
//                        .onErrorMap( RuntimeException.class, throwable -> {
//        return  new RuntimeException(throwable);
//                })
//                        .onErrorResume(RuntimeException.class , tr -> {
//        return Mono.error(tr);
//                });