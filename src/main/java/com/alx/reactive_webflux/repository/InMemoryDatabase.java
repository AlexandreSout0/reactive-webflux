package com.alx.reactive_webflux.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.lang.Thread.sleep;

@Component
@RequiredArgsConstructor
public class InMemoryDatabase  implements Database {
    private static final Map<String, String> DATABASE = new ConcurrentHashMap<>();

    private final ObjectMapper mapper;

    @Override
    @SneakyThrows
    public <T> T save( final String key, final T value) {
        final var data = this.mapper.writeValueAsString(value);
        DATABASE.put(key, data);
        sleeps(5_000);
        return value;
    }

    @Override
    public <T> Optional<T> get(String key, final Class<T> clazz) {
        final String json = DATABASE.get(key);
        sleeps(1_000);
        return Optional.ofNullable(json)
            .map(data -> {
                try {
                    return mapper.readValue(data, clazz);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    private void sleeps(final long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}
