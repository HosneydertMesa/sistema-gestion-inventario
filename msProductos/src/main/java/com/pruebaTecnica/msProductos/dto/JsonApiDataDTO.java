package com.pruebaTecnica.msProductos.dto;

import lombok.Data;

@Data
public class JsonApiDataDTO<T> {
    private String type = "products"; // El tipo de recurso
    private String id;
    private T attributes;

    public JsonApiDataDTO(String id, T attributes) {
        this.id = id;
        this.attributes = attributes;
    }
}