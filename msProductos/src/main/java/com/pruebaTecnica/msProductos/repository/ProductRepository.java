package com.pruebaTecnica.msProductos.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.pruebaTecnica.msProductos.entity.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    Optional<Object> findById(Long id);

    Product save(Product product);

    void delete(Product product);
}
