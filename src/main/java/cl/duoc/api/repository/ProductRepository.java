package cl.duoc.api.repository;

import cl.duoc.api.dto.ProductResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

@Repository
public class ProductRepository {
    private final RestClient restClient;

    public ProductRepository(
            RestClient.Builder restClientBuilder,
            @Value("${microservices.productos.base-url}") String baseUrl) {
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public List<ProductResponse> obtenerTodos() {
        return restClient
                .get()
                .uri("/api/productos")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ProductResponse>>() {
                });
    }
}