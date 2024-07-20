package com.alx.reactive_webflux;

import com.alx.reactive_webflux.model.PubSubMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Sinks;

@SpringBootApplication
public class ReactiveWebfluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveWebfluxApplication.class, args);
	}

	@Bean
	public Sinks.Many<PubSubMessage> sink(){
		return Sinks.many().multicast()
				.onBackpressureBuffer(1000);
	}

}
