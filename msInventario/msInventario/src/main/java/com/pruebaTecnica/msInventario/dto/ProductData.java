package com.pruebaTecnica.msInventario.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

// Representa el objeto "data" que viene en la respuesta del servicio de productos
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductData {
    private String id;
    private ProductAttributes attributes;
}