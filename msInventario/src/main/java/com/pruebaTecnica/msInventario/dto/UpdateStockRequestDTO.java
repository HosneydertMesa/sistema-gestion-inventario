package com.pruebaTecnica.msInventario.dto;

import lombok.Data;

// DTO para el cuerpo de la petición de actualización de stock
@Data
public class UpdateStockRequestDTO {
    // Puede ser positivo (venta) o negativo (reposición)
    private Integer quantityChange;
}