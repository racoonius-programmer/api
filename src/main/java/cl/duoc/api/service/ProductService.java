package cl.duoc.api.service;

import cl.duoc.api.dto.ProductResponse;
import cl.duoc.api.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> obtenerProductos() {
        return productRepository.obtenerTodos();
    }
}