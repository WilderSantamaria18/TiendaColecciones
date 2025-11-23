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
		
		modelo.addAttribute("titulo", ConstantesApp.TITLE_PRENDAS_LISTA);
		
		// Obtener todas las prendas inicialmente
		var todasLasPrendas = prendasServicio.listarTodas();
		
		// Aplicar filtros si existen
		if (coleccionId != null) {
			todasLasPrendas = todasLasPrendas.stream()
				.filter(p -> p.getColeccion() != null && p.getColeccion().getIdColeccion().equals(coleccionId))
				.toList();
		}
		
		if (estado != null && !estado.trim().isEmpty()) {
			todasLasPrendas = todasLasPrendas.stream()
				.filter(p -> estado.equals(p.getEstado()))
				.toList();
		}
		
		if (busqueda != null && !busqueda.trim().isEmpty()) {
			String busquedaLower = busqueda.toLowerCase().trim();
			todasLasPrendas = todasLasPrendas.stream()
				.filter(p -> p.getNombre().toLowerCase().contains(busquedaLower) ||
						   (p.getTalla() != null && p.getTalla().toLowerCase().contains(busquedaLower)))
				.toList();
		}
		
		modelo.addAttribute("prendas", todasLasPrendas);
		modelo.addAttribute("colecciones", coleccionesServicio.listadoActivos());
		

		modelo.addAttribute("filtroColeccionId", coleccionId);
		modelo.addAttribute("filtroEstado", estado);
		modelo.addAttribute("filtroBusqueda", busqueda);
		
		return "prendas/lista";
	}

	@GetMapping("/registroPrenda")
	public String registroPrenda(Model modelo, @RequestParam(required = false) Long coleccionId) {
		modelo.addAttribute("titulo", ConstantesApp.TITLE_PRENDAS_FORM);
		PrendasDto prenda = new PrendasDto();
		if (coleccionId != null) {
			prenda.setIdColeccion(coleccionId);
		}
		modelo.addAttribute("prenda", prenda);
		modelo.addAttribute("colecciones", coleccionesServicio.listadoActivos());
		return "prendas/form";
	}

	@GetMapping("/editar/{id}")
	public String editarPrenda(@PathVariable Long id, Model modelo, RedirectAttributes redirectAttributes) {
		try {
			PrendasEntity prenda = prendasServicio.obtenerPorId(id).orElseThrow(() -> new IllegalArgumentException("Prenda no encontrada"));
			PrendasDto prendasDto = PrendasMapper.toDto(prenda);
			modelo.addAttribute("titulo", "Editar Prenda");
			modelo.addAttribute("prenda", prendasDto);
			modelo.addAttribute("colecciones", coleccionesServicio.listadoActivos());
			return "prendas/form";
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/web/prendas/listar_prendas";
		}
	}

	@PostMapping("/guardar")
	public String guardar(@Valid @ModelAttribute("prenda") PrendasDto prendasDto, BindingResult result, 
			Model modelo, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			modelo.addAttribute("titulo", prendasDto.getIdPrenda() == null ? 
					ConstantesApp.TITLE_PRENDAS_FORM : "Editar Prenda");
			modelo.addAttribute("colecciones", coleccionesServicio.listadoActivos());
			return "prendas/form";
		}
		try {
			ColeccionesEntity coleccion = coleccionesServicio.buscarPorId(prendasDto.getIdColeccion())
					.orElseThrow(() -> new IllegalArgumentException("Colección no encontrada"));
			
			if (prendasDto.getIdPrenda() != null) {
				PrendasEntity existente = prendasServicio.obtenerPorId(prendasDto.getIdPrenda())
						.orElseThrow(() -> new IllegalArgumentException("Prenda no encontrada"));
				PrendasMapper.updateEntity(prendasDto, existente, coleccion);
				prendasServicio.actualizar(existente);
				redirectAttributes.addFlashAttribute("success", "Prenda actualizada exitosamente");
			} else {
				PrendasEntity nueva = PrendasMapper.toEntity(prendasDto, coleccion);
				prendasServicio.guardar(nueva);
				redirectAttributes.addFlashAttribute("success", "Prenda registrada exitosamente");
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
		}
		return "redirect:/web/prendas/listar_prendas";
	}

	@PostMapping("/cambiar-estado/{id}")
	public String cambiarEstado(@PathVariable Long id, @RequestParam String estado, RedirectAttributes redirectAttributes) {
		try {
			PrendasEntity prenda = prendasServicio.obtenerPorId(id).orElseThrow();
			prenda.setEstado(estado);
			prendasServicio.actualizar(prenda);
			redirectAttributes.addFlashAttribute("success", "Estado actualizado exitosamente");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
		}
		return "redirect:/web/prendas/listar_prendas";
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleBadRequest(IllegalArgumentException ex, Model model) {
		model.addAttribute("error", ex.getMessage());
		model.addAttribute("status", 400);
		return "error/400";
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleServerError(RuntimeException ex, Model model) {
		model.addAttribute("error", "Error interno del servidor");
		model.addAttribute("status", 500);
		return "error/500";
	}
}
