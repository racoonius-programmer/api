package cl.duoc.api.dto;

public record ItemCarritoRequest(
    String nombre,
    Double precio,
    Integer cantidad
) {}