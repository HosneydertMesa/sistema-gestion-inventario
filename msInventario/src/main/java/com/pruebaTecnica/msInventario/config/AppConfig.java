package com.pruebaTecnica.msInventario.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(3000)) // Timeout para establecer la conexión
                .setReadTimeout(Duration.ofMillis(3000)) // Timeout para recibir la respuesta
                .build();

    }
}