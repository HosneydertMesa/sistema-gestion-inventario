package com.pruebaTecnica.msInventario.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductData {
    private Long id;
    private ProductAttributes attributes;

}
