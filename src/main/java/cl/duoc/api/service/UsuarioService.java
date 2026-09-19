package cl.duoc.api.service;

import cl.duoc.api.dto.UsuarioResponse;
import cl.duoc.api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponse obtenerPerfil(String oid, String email, String rol) {
        return usuarioRepository.buscarPorOid(oid, email, rol);
    }
}
