package com.pruebaTecnica.msInventario.service;

import com.pruebaTecnica.msInventario.dto.FullInventoryDTO;
import com.pruebaTecnica.msInventario.entity.Inventory;

public interface InventoryService {
    Inventory updateStock(Long productId, Integer quantityChange);

    // DTO para la respuesta combinada
    FullInventoryDTO getInventoryDetails(Long productId);
}