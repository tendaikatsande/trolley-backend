package zw.co.trolley.OrderService.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import zw.co.trolley.OrderService.utils.JacksonLocalDateTimeSerializer;

import java.time.LocalDateTime;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("Registering custom LocalDateTime serializer");
        // Register custom serializer
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new JacksonLocalDateTimeSerializer());

        objectMapper.registerModule(new JavaTimeModule()); // Support Java Time API
        objectMapper.registerModule(module); // Register our custom serializer

        return objectMapper;
    }
}
