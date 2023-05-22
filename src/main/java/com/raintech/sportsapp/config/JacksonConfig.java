package com.raintech.sportsapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Ignore Hibernate proxy properties
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Configure support for Java 8 date/time types (optional)
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }
}
