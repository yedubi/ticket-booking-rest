package org.booking.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collection;
import java.util.Map;

public class Mapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> T convert(Map<Long, Object> map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }

    public <T> T convert(Collection<Object> events) {
        return objectMapper.convertValue(events, new TypeReference<>() {
        });
    }

    public <T> T convert(T event) {
        return objectMapper.convertValue(event, new TypeReference<>() {
        });
    }

    public ObjectMapper getObjectMapper(){
        return objectMapper;
    }
}
