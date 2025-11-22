package com.santamaria.tienda.Continua3.exception;

/**
 * Excepción personalizada para datos inválidos en formularios
 */
public class DatosInvalidosException extends RuntimeException {
    
    public DatosInvalidosException(String mensaje) {
        super(mensaje);
    }
    
    public DatosInvalidosException(String campo, String motivo) {
        super("El campo '" + campo + "' es inválido: " + motivo);
    }
}
