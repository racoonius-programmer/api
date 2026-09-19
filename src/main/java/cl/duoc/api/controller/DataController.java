package cl.duoc.api.controller;

import cl.duoc.api.dto.UsuarioResponse;
import cl.duoc.api.service.UsuarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DataController {

    private final UsuarioService usuarioService;

    public DataController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/perfil")
    public UsuarioResponse perfil(@AuthenticationPrincipal Jwt jwt) {
        String oid = jwt.getClaimAsString("oid");
        String email = jwt.getClaimAsString("preferred_username");
        String nombre = jwt.getClaimAsString("name");

        return usuarioService.obtenerPerfil(oid, email, nombre);
    }
}