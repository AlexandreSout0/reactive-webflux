package com.alx.reactive_webflux.repository;

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

@Component
@RequiredArgsConstructor
public class InMemoryDatabase  implements Database {
    private static final Map<String, String> DATABASE = new ConcurrentHashMap<>();

    private final ObjectMapper mapper;

    @Override
    @SneakyThrows
    public <T> T save( final String key, final T value) {
        final this.mapper.writeValueAsString(value);
        return null;
    }

    @Override
    public <T> Optional<T> get(String key) {
        return Optional.empty();
    }
}
