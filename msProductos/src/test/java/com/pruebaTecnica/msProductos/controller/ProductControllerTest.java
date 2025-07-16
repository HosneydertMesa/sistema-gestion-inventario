package com.pruebaTecnica.msProductos.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebaTecnica.msProductos.entity.Product;
import com.pruebaTecnica.msProductos.service.ProductService;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class ControllerTestConfig {
        @Bean
        public ProductService productService() {
            return mock(ProductService.class); // Mock del servicio
        }
    }

    @Test
    void whenCreateProduct_thenReturns201Created() throws Exception {

        Product productToCreate = new Product();
        productToCreate.setNombre("Teclado Mecánico");
        productToCreate.setPrecio(99.99);

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setNombre("Teclado Mecánico");
        savedProduct.setPrecio(99.99);

        // Simular el comportamiento del servicio
        when(productService.createProduct(any(Product.class))).thenReturn(savedProduct);

        // Realizar la petición POST al endpoint de creación de productos
        mockMvc.perform(post("/api/v1/products")
                .header("X-API-KEY", "mi-api-key-secreta")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.attributes.nombre").value("Teclado Mecánico"));
    }
}