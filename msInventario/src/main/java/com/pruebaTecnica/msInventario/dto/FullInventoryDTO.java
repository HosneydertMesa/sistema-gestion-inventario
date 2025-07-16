package com.pruebaTecnica.msInventario.dto;

import lombok.Data;

// DTO para la respuesta combinada
@Data
public class FullInventoryDTO {
    private Long productId;
    private Integer cantidad;
    private String nombreProducto;
    private Double precioProducto;
}