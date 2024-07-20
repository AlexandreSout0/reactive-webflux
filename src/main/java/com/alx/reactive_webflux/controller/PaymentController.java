package com.alx.reactive_webflux.controller;


import com.alx.reactive_webflux.model.Payment;
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
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentRepository paymentRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Payment> createPayment(@RequestBody final NewPaymentInput input){
        final String userId = input.getUserId();
        log.info("Payment to be processed {}", userId);
        return this.paymentRepository.createPayment(userId)
                .doOnNext(next -> log.info("Payment Processed {} ", userId));

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