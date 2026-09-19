package cl.duoc.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.duoc.api.dto.UsuarioResponse;
import cl.duoc.api.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

class DataControllerTest {

    @Test
    void perfilDebeDelegarAlServicioConOidEmailYNombre() {
        UsuarioService usuarioService = mock(UsuarioService.class);
        Jwt jwt = mock(Jwt.class);

        when(jwt.getClaimAsString("oid")).thenReturn("oid-123");
        when(jwt.getClaimAsString("preferred_username")).thenReturn("ana@duoc.cl");
        when(jwt.getClaimAsString("name")).thenReturn("Ana");

        UsuarioResponse esperado = new UsuarioResponse("oid-123", "Ana", "ana@duoc.cl", "user");
        when(usuarioService.obtenerPerfil("oid-123", "ana@duoc.cl", "Ana")).thenReturn(esperado);

        DataController controller = new DataController(usuarioService);
        UsuarioResponse resultado = controller.perfil(jwt);

        assertEquals(esperado, resultado);
        verify(usuarioService).obtenerPerfil("oid-123", "ana@duoc.cl", "Ana");
    }
}
