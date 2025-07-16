package com.pruebaTecnica.msInventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import com.pruebaTecnica.msInventario.entity.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    /**
     * Busca un registro de inventario por el ID del producto.
     * 
     * @param productId El ID del producto del microservicio de Productos.
     * @return Un Optional que contiene el inventario si se encuentra.
     */
    Optional<Inventory> findByProductId(Long productId);
}