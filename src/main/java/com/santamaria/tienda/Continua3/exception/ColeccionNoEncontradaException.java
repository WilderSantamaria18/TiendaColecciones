package com.santamaria.tienda.Continua3.exception;

/**
 * Excepción cuando no se encuentra una colección
 */
public class ColeccionNoEncontradaException extends RuntimeException {
    
    public ColeccionNoEncontradaException(String mensaje) {
        super(mensaje);
    }
    
    public ColeccionNoEncontradaException(Long id) {
        super("No se encontró la colección con ID: " + id);
    }
}
