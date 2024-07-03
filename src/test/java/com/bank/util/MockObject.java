package com.bank.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public final class MockObject {

    public static <T> T getObjectFromFile(String path, Class<T> objectClass) throws IOException {
        return getDisable().readValue(new ClassPathResource(path).getFile(), objectClass);
    }

    public static <T> List<T> getListFromFile(String path, Class<T> objectClass) throws IOException {
        var objectMapper = getDisable();

        return objectMapper.readValue(new ClassPathResource(path).getFile(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, objectClass));
    }

    private static ObjectMapper getDisable() {
        return new ObjectMapper().registerModule(new JavaTimeModule()).configure(WRITE_DATES_AS_TIMESTAMPS, false)
                .disable(FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
