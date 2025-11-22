package com.santamaria.tienda.Continua3.exception;

/**
 * Excepción personalizada para prenda sin colección
 * RNF19.V4: No permitir registrar prenda sin colección asociada
 */
public class PrendaSinColeccionException extends RuntimeException {
    
    public PrendaSinColeccionException(String mensaje) {
        super(mensaje);
    }
    
    public PrendaSinColeccionException() {
        super("No se puede registrar una prenda sin una colección asociada");
    }
}
