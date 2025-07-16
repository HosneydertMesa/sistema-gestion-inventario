package com.pruebaTecnica.msProductos.controller;

import com.pruebaTecnica.msProductos.dto.JsonApiDataDTO;
import com.pruebaTecnica.msProductos.dto.JsonApiSingleResponseDTO;
import com.pruebaTecnica.msProductos.dto.ProductAttributesDTO;
import com.pruebaTecnica.msProductos.dto.ProductDTO;
import com.pruebaTecnica.msProductos.entity.Product;
import com.pruebaTecnica.msProductos.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "Get a product by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        // Convertimos la entidad a un DTO antes de enviarla
        ProductDTO productDTO = new ProductDTO(product.getId(), product.getNombre(), product.getPrecio());
        return ResponseEntity.ok(productDTO);
    }

    @Operation(summary = "Create a new product")
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product productRequest) {

        // 1. Llama al servicio para guardar el producto
        Product createdProduct = productService.createProduct(productRequest);

        ProductAttributesDTO attributes = new ProductAttributesDTO();
        attributes.setNombre(createdProduct.getNombre());
        attributes.setPrecio(createdProduct.getPrecio());

        JsonApiDataDTO<ProductAttributesDTO> data = new JsonApiDataDTO<>(
                String.valueOf(createdProduct.getId()),
                attributes);

        JsonApiSingleResponseDTO<ProductAttributesDTO> responseBody = new JsonApiSingleResponseDTO<>(data);

        // 3. Devuelve el DTO en el cuerpo de la respuesta con el estado 201
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing product by its ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        // El servicio ya se encarga de verificar si el producto existe.
        Product updatedProduct = productService.updateProduct(id, productDetails);

        ProductAttributesDTO attributes = new ProductAttributesDTO();
        attributes.setNombre(updatedProduct.getNombre());
        attributes.setPrecio(updatedProduct.getPrecio());

        JsonApiDataDTO<ProductAttributesDTO> data = new JsonApiDataDTO<>(
                String.valueOf(updatedProduct.getId()),
                attributes);

        JsonApiSingleResponseDTO<ProductAttributesDTO> responseBody = new JsonApiSingleResponseDTO<>(data);

        // Devuelve el DTO actualizado con el estado 200 OK
        return ResponseEntity.ok(responseBody);
    }

    @Operation(summary = "Delete a product by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        // Llama al servicio para eliminar el producto.
        // El servicio se encargará de lanzar una excepción si no lo encuentra.
        productService.deleteProduct(id);

        // Devuelve una respuesta vacía con el estado 204 No Content.
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List all products with simple pagination")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProducts(Pageable pageable) {
        // Llama al servicio, pasándole los parámetros de paginación
        Page<Product> productPage = productService.getAllProducts(pageable);

        // Mapea la lista de productos a una lista de DTOs con formato JSON:API
        List<JsonApiDataDTO<ProductAttributesDTO>> dataList = productPage.getContent().stream()
                .map(product -> {
                    ProductAttributesDTO attributes = new ProductAttributesDTO();
                    attributes.setNombre(product.getNombre());
                    attributes.setPrecio(product.getPrecio());
                    return new JsonApiDataDTO<>(String.valueOf(product.getId()), attributes);
                })
                .collect(Collectors.toList());

        // Crea el objeto 'meta' con la información de paginación
        Map<String, Object> meta = new HashMap<>();
        meta.put("currentPage", productPage.getNumber());
        meta.put("totalPages", productPage.getTotalPages());
        meta.put("pageSize", productPage.getSize());
        meta.put("totalElements", productPage.getTotalElements());

        // Construye la respuesta final en formato JSON:API
        Map<String, Object> response = new HashMap<>();
        response.put("data", dataList);
        response.put("meta", meta);

        return ResponseEntity.ok(response);
    }
}
