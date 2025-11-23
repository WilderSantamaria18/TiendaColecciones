package com.santamaria.tienda.Continua3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.santamaria.tienda.Continua3.entity.PrendasEntity;
import com.santamaria.tienda.Continua3.entity.ColeccionesEntity;
import com.santamaria.tienda.Continua3.dto.PrendasDto;
import com.santamaria.tienda.Continua3.mapper.PrendasMapper;
import com.santamaria.tienda.Continua3.service.PrendasService;
import com.santamaria.tienda.Continua3.service.ColeccionesService;
import com.santamaria.tienda.Continua3.util.ConstantesApp;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/web/prendas")
public class PrendasController {

	private final PrendasService prendasServicio;
	private final ColeccionesService coleccionesServicio;

	public PrendasController(PrendasService prendasServicio, ColeccionesService coleccionesServicio) {
		this.prendasServicio = prendasServicio;
		this.coleccionesServicio = coleccionesServicio;
	}

	@GetMapping({"/listar_prendas", ""})
	public String listarPrendas(Model modelo,
			@RequestParam(required = false) Long coleccionId,
			@RequestParam(required = false) String estado,
			@RequestParam(required = false) String busqueda) {
		
		// Configurar título de la página
		modelo.addAttribute("titulo", ConstantesApp.TITLE_PRENDAS_LISTA);
		
		// Obtener lista completa de prendas desde la base de datos
		var prendas = prendasServicio.listarTodas();
		
		// Filtrar por colección si se especifica
		if (coleccionId != null) {
			prendas = prendas.stream()
				.filter(prenda -> prenda.getColeccion() != null && 
						prenda.getColeccion().getIdColeccion().equals(coleccionId))
				.toList();
		}
		
		// Filtrar por estado si se especifica
		if (estado != null && !estado.trim().isEmpty()) {
			prendas = prendas.stream()
				.filter(prenda -> estado.equals(prenda.getEstado()))
				.toList();
		}
		
		// Filtrar por búsqueda en nombre o talla
		if (busqueda != null && !busqueda.trim().isEmpty()) {
			String textoBusqueda = busqueda.toLowerCase().trim();
			prendas = prendas.stream()
				.filter(prenda -> prenda.getNombre().toLowerCase().contains(textoBusqueda) ||
						   (prenda.getTalla() != null && prenda.getTalla().toLowerCase().contains(textoBusqueda)))
				.toList();
		}
		
		// Enviar datos a la vista
		modelo.addAttribute("prendas", prendas);
		modelo.addAttribute("colecciones", coleccionesServicio.listadoActivos());
		modelo.addAttribute("filtroColeccionId", coleccionId);
		modelo.addAttribute("filtroEstado", estado);
		modelo.addAttribute("filtroBusqueda", busqueda);
		
		return "prendas/lista";
	}

	@GetMapping("/registroPrenda")
	public String registroPrenda(Model modelo, @RequestParam(required = false) Long coleccionId) {
		// Configurar título de la página
		modelo.addAttribute("titulo", ConstantesApp.TITLE_PRENDAS_FORM);
		
		// Crear nuevo objeto DTO para el formulario
		PrendasDto nuevaPrenda = new PrendasDto();
		
		// Si viene una colección pre-seleccionada, la asignamos
		if (coleccionId != null) {
			nuevaPrenda.setIdColeccion(coleccionId);
		}
		
		// Enviar datos al formulario
		modelo.addAttribute("prenda", nuevaPrenda);
		modelo.addAttribute("colecciones", coleccionesServicio.listadoActivos());
		
		return "prendas/form";
	}

	@GetMapping("/editar/{id}")
	public String editarPrenda(@PathVariable Long id, Model modelo, RedirectAttributes redirectAttributes) {
		// Buscar la prenda en la base de datos
		PrendasEntity prendaEntity = prendasServicio.obtenerPorId(id).orElse(null);
		
		if (prendaEntity == null) {
			redirectAttributes.addFlashAttribute("error", "Prenda no encontrada");
			return "redirect:/web/prendas/listar_prendas";
		}
		
		// Convertir entidad a DTO para el formulario
		PrendasDto prendaDto = PrendasMapper.toDto(prendaEntity);
		
		// Configurar vista de edición
		modelo.addAttribute("titulo", "Editar Prenda");
		modelo.addAttribute("prenda", prendaDto);
		modelo.addAttribute("colecciones", coleccionesServicio.listadoActivos());
		
		return "prendas/form";
	}

	@PostMapping("/guardar")
	public String guardar(@Valid @ModelAttribute("prenda") PrendasDto prendaDto, 
						  BindingResult erroresValidacion, 
						  Model modelo, 
						  RedirectAttributes mensajesRedireccion) {
		
		// Si hay errores de validación, volver al formulario
		if (erroresValidacion.hasErrors()) {
			// Determinar título según si es nueva prenda o edición
			String titulo = (prendaDto.getIdPrenda() == null) ? 
						ConstantesApp.TITLE_PRENDAS_FORM : "Editar Prenda";
			
			modelo.addAttribute("titulo", titulo);
			modelo.addAttribute("colecciones", coleccionesServicio.listadoActivos());
			return "prendas/form";
		}
		
		// Buscar la colección asociada
		ColeccionesEntity coleccion = coleccionesServicio.buscarPorId(prendaDto.getIdColeccion()).orElse(null);
		
		if (coleccion == null) {
			mensajesRedireccion.addFlashAttribute("error", "Colección no encontrada");
			return "redirect:/web/prendas/listar_prendas";
		}
		
		try {
			// Verificar si es edición o nuevo registro
			if (prendaDto.getIdPrenda() != null) {
				// EDITAR PRENDA EXISTENTE
				PrendasEntity prendaExistente = prendasServicio.obtenerPorId(prendaDto.getIdPrenda()).orElse(null);
				
				if (prendaExistente == null) {
					mensajesRedireccion.addFlashAttribute("error", "Prenda no encontrada");
					return "redirect:/web/prendas/listar_prendas";
				}
				
				// Actualizar datos de la prenda usando el mapper
				PrendasMapper.updateEntity(prendaDto, prendaExistente, coleccion);
				prendasServicio.actualizar(prendaExistente);
				
				mensajesRedireccion.addFlashAttribute("success", "Prenda actualizada exitosamente");
				
			} else {
				// CREAR NUEVA PRENDA
				PrendasEntity nuevaPrenda = PrendasMapper.toEntity(prendaDto, coleccion);
				prendasServicio.guardar(nuevaPrenda);
				
				mensajesRedireccion.addFlashAttribute("success", "Prenda registrada exitosamente");
			}
			
		} catch (Exception e) {
			// Manejar errores y mostrar mensaje
			mensajesRedireccion.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
		}
		
		// Redirigir a la lista de prendas
		return "redirect:/web/prendas/listar_prendas";
	}

	@PostMapping("/cambiar-estado/{id}")
	public String cambiarEstado(@PathVariable Long id, 
							    @RequestParam String estado, 
							    RedirectAttributes mensajes) {
		try {
			// Buscar la prenda por ID
			PrendasEntity prenda = prendasServicio.obtenerPorId(id).orElseThrow();
			
			// Cambiar el estado y guardar
			prenda.setEstado(estado);
			prendasServicio.actualizar(prenda);
			
			mensajes.addFlashAttribute("success", "Estado actualizado exitosamente");
			
		} catch (Exception e) {
			mensajes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
		}
		
		return "redirect:/web/prendas/listar_prendas";
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
