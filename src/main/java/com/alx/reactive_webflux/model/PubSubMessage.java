package com.alx.reactive_webflux.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PubSubMessage {
    String key;
    String value;

}
