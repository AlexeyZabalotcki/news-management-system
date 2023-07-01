package ru.clevertec.gateway_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.gateway_service.feign_client.CustomErrorDecoder;

@Configuration
public class ReactiveFeignClientConfiguration {

    @Bean
    public CustomErrorDecoder customErrorDecoder() {
        return new CustomErrorDecoder();
    }

}
