package cl.duoc.api.service;

import cl.duoc.api.dto.PedidoRequest;
import cl.duoc.api.dto.PedidoResponse;
import cl.duoc.api.dto.UsuarioResponse;
import cl.duoc.api.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioService usuarioService;

    public PedidoService(PedidoRepository pedidoRepository, UsuarioService usuarioService) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioService = usuarioService;
    }

    public List<PedidoResponse> obtenerPedidos(String oid, String email, String nombre) {
        // 1. Preguntamos al ms-usuario qué rol tiene esta persona (y lo crea si no existe)
        UsuarioResponse usuario = usuarioService.obtenerPerfil(oid, email, nombre);

        // 2. Tomamos la decisión
        if ("ADMIN".equalsIgnoreCase(usuario.rol())) {
            // Si es admin, le devolvemos todos los pedidos de la base de datos
            return pedidoRepository.obtenerTodos();
        } else {
            // Si es un usuario normal, solo le devolvemos los suyos
            return pedidoRepository.obtenerPorUsuario(oid);
        }
    }

    public PedidoResponse crearPedido(String oid, PedidoRequest request) {
        return pedidoRepository.crearPedido(oid, request);
    }
}