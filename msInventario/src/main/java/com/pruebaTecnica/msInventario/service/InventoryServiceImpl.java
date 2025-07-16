package com.pruebaTecnica.msInventario.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import com.pruebaTecnica.msInventario.entity.*;
import com.pruebaTecnica.msInventario.repository.InventoryRepository;
import com.pruebaTecnica.msInventario.dto.*;

@Service
public class InventoryServiceImpl implements InventoryService {

        @Autowired
        private InventoryRepository inventoryRepository;

        @Autowired
        private RestTemplate restTemplate;

        @Value("${services.products.url}")
        private String productsServiceUrl;

        @Value("${services.products.api-key}")
        private String productsApiKey;

        @Override
        public FullInventoryDTO getInventoryDetails(Long productId) {

                // Buscar el inventario. Si no existe, se le asigna 0.
                // Esto evita el error "Inventario no encontrado".
                Inventory inventory = inventoryRepository.findByProductId(productId)
                                .orElseGet(() -> {
                                        Inventory newInventory = new Inventory();
                                        newInventory.setProductId(productId);
                                        newInventory.setCantidad(0);
                                        return inventoryRepository.save(newInventory);
                                });

                // Preparar los headers con la API Key para llamar a msProductos
                HttpHeaders headers = new HttpHeaders();
                headers.set("X-API-KEY", productsApiKey);
                HttpEntity<String> entity = new HttpEntity<>(headers);

                // llamada al microservicio de productos
                ResponseEntity<ProductDTO> response = restTemplate.exchange(
                                productsServiceUrl + "/api/v1/products/" + productId,
                                HttpMethod.GET,
                                entity,
                                ProductDTO.class); //

                ProductDTO productDetails = response.getBody();
                if (productDetails == null) {
                        throw new RuntimeException(
                                        "No se recibieron detalles del producto desde el microservicio de productos.");
                }

                // 3. Combinar la información y devolver un DTO de respuesta completo
                FullInventoryDTO fullDetails = new FullInventoryDTO();
                fullDetails.setProductId(productId);
                fullDetails.setCantidad(inventory.getCantidad());
                fullDetails.setNombreProducto(productDetails.getNombre());
                fullDetails.setPrecioProducto(productDetails.getPrecio());

                return fullDetails;
        }

        @Override
        public Inventory updateStock(Long productId, Integer quantityChange) {
                // Aplicar Lazy Initialization aquí para robustecer el método
                Inventory inventory = inventoryRepository.findByProductId(productId)
                                .orElseGet(() -> {
                                        Inventory newInventory = new Inventory();
                                        newInventory.setProductId(productId);
                                        newInventory.setCantidad(0);
                                        return inventoryRepository.save(newInventory);
                                });

                int newQuantity = inventory.getCantidad() + quantityChange;
                inventory.setCantidad(newQuantity);

                return inventoryRepository.save(inventory);
        }
}
