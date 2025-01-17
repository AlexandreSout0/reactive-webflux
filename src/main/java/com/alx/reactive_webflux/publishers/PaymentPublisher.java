package com.alx.reactive_webflux.publishers;

import com.alx.reactive_webflux.model.Payment;
import com.alx.reactive_webflux.model.PubSubMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class PaymentPublisher {
    private final Sinks.Many<PubSubMessage> sink;
    private final ObjectMapper mapper;

    public Mono<Payment> onPaymentCreate( final Payment payment){
        return Mono.fromCallable(() -> {
            final String userId = payment.getUserId();
            final String data = mapper.writeValueAsString(payment);
            return new PubSubMessage(userId, data);
        })
                .subscribeOn(Schedulers.parallel())
                .doOnNext(messsage -> this.sink.tryEmitNext(messsage))
                .thenReturn(payment);
    }
}
