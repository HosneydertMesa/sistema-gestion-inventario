package com.pruebaTecnica.msInventario.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductAttributes {
    private String nombre;
    private Double precio;
}
