package com.pruebaTecnica.msProductos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.pruebaTecnica.msProductos.entity.Product;

public interface ProductService {
    Product createProduct(Product product);

    Page<Product> getAllProducts(Pageable pageable);

    Product getProductById(Long id);

    Product updateProduct(Long id, Product productDetails);

    void deleteProduct(Long id);
}
