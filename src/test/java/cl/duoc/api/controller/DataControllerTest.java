package cl.duoc.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.duoc.api.dto.ProductResponse;
import cl.duoc.api.dto.UsuarioResponse;
import cl.duoc.api.service.ProductService;
import cl.duoc.api.service.UsuarioService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

class DataControllerTest {

    @Test
    void perfilDebeDelegarAlServicioConOidEmailYNombre() {
        UsuarioService usuarioService = mock(UsuarioService.class);
        ProductService productService = mock(ProductService.class);
        Jwt jwt = mock(Jwt.class);

        when(jwt.getClaimAsString("oid")).thenReturn("oid-123");
        when(jwt.getClaimAsString("preferred_username")).thenReturn("ana@duoc.cl");
        when(jwt.getClaimAsString("name")).thenReturn("Ana");

        UsuarioResponse esperado = new UsuarioResponse("oid-123", "Ana", "ana@duoc.cl", "user");
        when(usuarioService.obtenerPerfil("oid-123", "ana@duoc.cl", "Ana")).thenReturn(esperado);

        DataController controller = new DataController(usuarioService, productService);
        UsuarioResponse resultado = controller.perfil(jwt);

        assertEquals(esperado, resultado);
        verify(usuarioService).obtenerPerfil("oid-123", "ana@duoc.cl", "Ana");
    }

    @Test
    void productosDebeDelegarAlServicio() {
        UsuarioService usuarioService = mock(UsuarioService.class);
        ProductService productService = mock(ProductService.class);
        List<ProductResponse> esperados = List.of(new ProductResponse("Producto", "Descripcion", 1000));
        when(productService.obtenerProductos()).thenReturn(esperados);

        DataController controller = new DataController(usuarioService, productService);

        assertEquals(esperados, controller.productos());
        verify(productService).obtenerProductos();
    }
}
