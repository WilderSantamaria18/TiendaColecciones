package com.santamaria.tienda.Continua3.config;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.santamaria.tienda.Continua3.exception.ColeccionNoEncontradaException;
import com.santamaria.tienda.Continua3.exception.DatosInvalidosException;
import com.santamaria.tienda.Continua3.exception.PrecioInvalidoException;
import com.santamaria.tienda.Continua3.exception.PrendaSinColeccionException;

/**
 * Manejador global de excepciones - Código hecho a mano
 * Captura errores y muestra páginas HTML personalizadas
 */
@ControllerAdvice
public class ConfigException {

    // Maneja errores de validación de formularios (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView manejarErroresValidacion(MethodArgumentNotValidException ex) {
        
        // Crear vista de error
        ModelAndView vista = new ModelAndView("error/400");
        
        // Recopilar todos los errores de validación
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String campo = error.getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
        });
        
        // Construir mensaje detallado
        StringBuilder detalles = new StringBuilder("Errores encontrados:\n");
        errores.forEach((campo, mensaje) -> {
            detalles.append("• ").append(campo).append(": ").append(mensaje).append("\n");
        });
        
        // Agregar datos a la vista
        vista.addObject("errorMessage", "Datos del formulario inválidos");
        vista.addObject("errorDetails", detalles.toString());
        vista.addObject("timestamp", LocalDateTime.now());
        
        return vista;
    }

    // RNF19.V3: Maneja precio inválido (debe ser mayor que cero)
    @ExceptionHandler(PrecioInvalidoException.class)
    public ModelAndView manejarPrecioInvalido(PrecioInvalidoException ex) {
        
        ModelAndView vista = new ModelAndView("error/400");
        
        vista.addObject("errorMessage", "❌ Precio Inválido");
        vista.addObject("errorDetails", ex.getMessage());
        vista.addObject("timestamp", LocalDateTime.now());
        
        return vista;
    }

    // RNF19.V4: Maneja prenda sin colección asociada
    @ExceptionHandler(PrendaSinColeccionException.class)
    public ModelAndView manejarPrendaSinColeccion(PrendaSinColeccionException ex) {
        
        ModelAndView vista = new ModelAndView("error/400");
        
        vista.addObject("errorMessage", "❌ Prenda sin Colección");
        vista.addObject("errorDetails", ex.getMessage());
        vista.addObject("timestamp", LocalDateTime.now());
        
        return vista;
    }

    // Maneja colección no encontrada (404)
    @ExceptionHandler(ColeccionNoEncontradaException.class)
    public ModelAndView manejarColeccionNoEncontrada(ColeccionNoEncontradaException ex) {
        
        ModelAndView vista = new ModelAndView("error/404");
        
        vista.addObject("errorMessage", "🔍 Colección No Encontrada");
        vista.addObject("errorDetails", ex.getMessage());
        vista.addObject("timestamp", LocalDateTime.now());
        
        return vista;
    }

    // RNF19.V1 y RNF19.V2: Maneja datos inválidos (nombre vacío, año fuera de rango)
    @ExceptionHandler(DatosInvalidosException.class)
    public ModelAndView manejarDatosInvalidos(DatosInvalidosException ex) {
        
        ModelAndView vista = new ModelAndView("error/400");
        
        vista.addObject("errorMessage", "❌ Datos Inválidos");
        vista.addObject("errorDetails", ex.getMessage());
        vista.addObject("timestamp", LocalDateTime.now());
        
        return vista;
    }

    // Maneja argumentos ilegales generales
    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView manejarArgumentoIlegal(IllegalArgumentException ex) {
        
        ModelAndView vista = new ModelAndView("error/400");
        
        vista.addObject("errorMessage", "⚠️ Argumento Inválido");
        vista.addObject("errorDetails", ex.getMessage());
        vista.addObject("timestamp", LocalDateTime.now());
        
        return vista;
    }

    // Maneja página no encontrada (404)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView manejarPaginaNoEncontrada(NoHandlerFoundException ex) {
        
        ModelAndView vista = new ModelAndView("error/404");
        
        vista.addObject("errorMessage", "🔍 Página No Encontrada");
        vista.addObject("errorDetails", "La ruta '" + ex.getRequestURL() + "' no existe");
        vista.addObject("timestamp", LocalDateTime.now());
        
        return vista;
    }

    // Maneja errores de integridad de base de datos
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ModelAndView manejarErrorIntegridadDatos(org.springframework.dao.DataIntegrityViolationException ex) {
        
        ModelAndView vista = new ModelAndView("error/400");
        
        vista.addObject("errorMessage", "⚠️ Error de Integridad de Datos");
        vista.addObject("errorDetails", "Los datos violan restricciones de la base de datos. Verifica duplicados o referencias inválidas.");
        vista.addObject("timestamp", LocalDateTime.now());
        
        return vista;
    }

    // Maneja cualquier otro error no capturado (500)
    @ExceptionHandler(Exception.class)
    public ModelAndView manejarErrorGeneral(Exception ex) {
        
        ModelAndView vista = new ModelAndView("error/500");
        
        vista.addObject("errorMessage", "💥 Error Interno del Servidor");
        vista.addObject("errorDetails", ex.getMessage());
        vista.addObject("timestamp", LocalDateTime.now());
        
        return vista;
    }
}
