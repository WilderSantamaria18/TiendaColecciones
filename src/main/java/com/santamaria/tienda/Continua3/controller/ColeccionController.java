package com.santamaria.tienda.Continua3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.santamaria.tienda.Continua3.entity.ColeccionesEntity;
import com.santamaria.tienda.Continua3.dto.ColeccionesDto;
import com.santamaria.tienda.Continua3.mapper.ColeccionesMapper;
import com.santamaria.tienda.Continua3.service.ColeccionesService;
import com.santamaria.tienda.Continua3.util.ConstantesApp;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/web/coleccion")
public class ColeccionController {

	private final ColeccionesService servicio;

	public ColeccionController(ColeccionesService servicio) {
		this.servicio = servicio;
	}

	@GetMapping({"/listado", ""})
	public String listarColecciones(Model modelo) {
		// Configurar título de la página
		modelo.addAttribute("titulo", ConstantesApp.TITLE_COLECCIONES_LISTA);
		
		// Obtener lista de colecciones desde el servicio
		modelo.addAttribute("listado", servicio.listado());
		
		return "colecciones/lista";
	}

	@GetMapping("/registroColeccion")
	public String formularioNuevaColeccion(Model modelo) {
		// Configurar título y objeto vacío para el formulario
		modelo.addAttribute("titulo", ConstantesApp.TITLE_COLECCIONES_FORM);
		modelo.addAttribute("coleccion", new ColeccionesDto());
		
		return "colecciones/form";
	}

	@GetMapping("/editar/{id}")
	public String editarColeccion(@PathVariable Long id, Model modelo, RedirectAttributes mensajes) {
		try {
			// Buscar la colección en la base de datos
			ColeccionesEntity coleccionEntity = servicio.buscarPorId(id)
					.orElseThrow(() -> new IllegalArgumentException("Colección no encontrada"));
			
			// Convertir entidad a DTO para el formulario
			ColeccionesDto coleccionDto = ColeccionesMapper.toDto(coleccionEntity);
			
			// Configurar vista de edición
			modelo.addAttribute("titulo", "Editar Colección");
			modelo.addAttribute("coleccion", coleccionDto);
			
			return "colecciones/form";
			
		} catch (IllegalArgumentException e) {
			// Si no se encuentra, mostrar error y redirigir
			mensajes.addFlashAttribute("error", e.getMessage());
			return "redirect:/web/coleccion/listado";
		}
	}

	@GetMapping("/detalle/{id}")
	public String verDetalleColeccion(@PathVariable Long id, Model modelo, RedirectAttributes mensajes) {
		try {
			// Buscar colección para mostrar detalle
			ColeccionesEntity coleccion = servicio.buscarPorId(id)
					.orElseThrow(() -> new IllegalArgumentException("Colección no encontrada"));
			
			// Configurar vista de detalle
			modelo.addAttribute("titulo", "Detalle de Colección");
			modelo.addAttribute("coleccion", coleccion);
			
			return "colecciones/detalle";
			
		} catch (IllegalArgumentException e) {
			mensajes.addFlashAttribute("error", e.getMessage());
			return "redirect:/web/coleccion/listado";
		}
	}

	@PostMapping("/guardar")
	public String guardarColeccion(@Valid @ModelAttribute("coleccion") ColeccionesDto coleccionDto, 
								   BindingResult erroresValidacion, 
								   Model modelo) {
		
		// Si hay errores de validación, volver al formulario
		if (erroresValidacion.hasErrors()) {
			// Determinar título según si es nueva colección o edición
			String titulo = (coleccionDto.getIdColeccion() == null) ? 
						ConstantesApp.TITLE_COLECCIONES_FORM : "Editar Colección";
			
			modelo.addAttribute("titulo", titulo);
			return "colecciones/form";
		}
		
		try {
			String mensajeExito;
			
			// Verificar si es edición o nuevo registro
			if (coleccionDto.getIdColeccion() != null) {
				// EDITAR COLECCIÓN EXISTENTE
				ColeccionesEntity coleccionExistente = servicio.buscarPorId(coleccionDto.getIdColeccion())
						.orElseThrow(() -> new IllegalArgumentException("Colección no encontrada"));
				
				// Actualizar datos usando el mapper
				ColeccionesMapper.updateEntity(coleccionDto, coleccionExistente);
				servicio.actualizar(coleccionExistente);
				
				mensajeExito = "¡Colección actualizada exitosamente!";
				
			} else {
				// CREAR NUEVA COLECCIÓN
				ColeccionesEntity nuevaColeccion = ColeccionesMapper.toEntity(coleccionDto);
				servicio.guardar(nuevaColeccion);
				
				mensajeExito = "¡Colección registrada exitosamente!";
			}
			
			// Redirigir con mensaje de éxito
			return "redirect:/web/coleccion/listado?success=" + 
				   java.net.URLEncoder.encode(mensajeExito, java.nio.charset.StandardCharsets.UTF_8);
			
		} catch (Exception e) {
			// Manejar errores
			String mensajeError = "Error al guardar: " + e.getMessage();
			return "redirect:/web/coleccion/listado?error=" + 
				   java.net.URLEncoder.encode(mensajeError, java.nio.charset.StandardCharsets.UTF_8);
		}
	}

	@PostMapping("/inactivar/{id}")
	public String inactivarColeccion(@PathVariable Long id) {
		try {
			// Inactivar colección usando el servicio
			servicio.inactivar(id);
			
			String mensajeExito = "¡Colección inactivada exitosamente!";
			return "redirect:/web/coleccion/listado?success=" + 
				   java.net.URLEncoder.encode(mensajeExito, java.nio.charset.StandardCharsets.UTF_8);
			
		} catch (Exception e) {
			String mensajeError = "Error al inactivar: " + e.getMessage();
			return "redirect:/web/coleccion/listado?error=" + 
				   java.net.URLEncoder.encode(mensajeError, java.nio.charset.StandardCharsets.UTF_8);
		}
	}

	// MANEJO DE ERRORES HTTP
	
	// Maneja errores de argumentos inválidos (Error 400)
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String manejarErrorArgumento(IllegalArgumentException error, Model modelo) {
		modelo.addAttribute("error", error.getMessage());
		modelo.addAttribute("status", 400);
		return "error/400";
	}

	// Maneja errores internos del servidor (Error 500)
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String manejarErrorServidor(RuntimeException error, Model modelo) {
		modelo.addAttribute("error", "Error interno del servidor");
		modelo.addAttribute("status", 500);
		return "error/500";
	}
}
