package cl.duoc.api.dto;
import java.util.List;

public record PedidoRequest(
    List<ItemCarritoRequest> items
) {}