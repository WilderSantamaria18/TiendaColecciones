package com.santamaria.tienda.Continua3.exception;

/**
 * Excepción  para precios inválidos
 * Validar que el precio de la prenda sea un número mayor que cero
 */
public class PrecioInvalidoException extends RuntimeException {
    
    public PrecioInvalidoException(String mensaje) {
        super(mensaje);
    }
    
    public PrecioInvalidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
