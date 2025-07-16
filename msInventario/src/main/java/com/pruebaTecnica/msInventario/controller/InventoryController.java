package com.pruebaTecnica.msInventario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pruebaTecnica.msInventario.dto.*;
import com.pruebaTecnica.msInventario.service.InventoryService;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/{productId}")
    public ResponseEntity<FullInventoryDTO> getInventoryDetails(@PathVariable Long productId) {
        FullInventoryDTO inventoryDetails = inventoryService.getInventoryDetails(productId);
        return ResponseEntity.ok(inventoryDetails);
    }

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<Void> updateStock(@PathVariable Long productId, @RequestBody UpdateStockRequestDTO request) {
        inventoryService.updateStock(productId, request.getQuantityChange());
        return ResponseEntity.ok().build();
    }
}
