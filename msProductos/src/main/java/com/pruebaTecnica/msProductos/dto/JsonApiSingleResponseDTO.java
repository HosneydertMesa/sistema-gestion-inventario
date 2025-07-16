package com.pruebaTecnica.msProductos.dto;

import lombok.Data;

@Data
public class JsonApiSingleResponseDTO<T> {
    private JsonApiDataDTO<T> data;

    public JsonApiSingleResponseDTO(JsonApiDataDTO<T> data) {
        this.data = data;
    }
}