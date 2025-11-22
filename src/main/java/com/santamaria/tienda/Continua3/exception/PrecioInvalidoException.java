package com.santamaria.tienda.Continua3.exception;

/**
 * Excepción personalizada para precios inválidos
 * RNF19.V3: Validar que el precio de la prenda sea un número mayor que cero
 */
public class PrecioInvalidoException extends RuntimeException {
    
    public PrecioInvalidoException(String mensaje) {
        super(mensaje);
    }
    
    public PrecioInvalidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
