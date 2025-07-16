package com.pruebaTecnica.msProductos.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

import com.pruebaTecnica.msProductos.entity.Product;
import com.pruebaTecnica.msProductos.repository.ProductRepository;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("security.api-key.secret", () -> "mi-api-key-secreta");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void whenPostProduct_thenProductIsCreatedInDb() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", "mi-api-key-secreta");

        String requestBody = "{\"nombre\": \"Silla Gamer Ergonómica\", \"precio\": 350.00}";
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        // Realizar la petición POST al endpoint de creación de productos
        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/products", request, String.class);
        // Verificar que la respuesta sea 201 Created
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        // Verificar que el producto se haya guardado en la base de datos
        Iterable<Product> products = productRepository.findAll(org.springframework.data.domain.Sort.unsorted());
        assertThat(products).hasSize(1);
        assertThat(products.iterator().next().getNombre()).isEqualTo("Silla Gamer Ergonómica");
    }
}
