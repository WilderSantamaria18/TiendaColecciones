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
import com.santamaria.tienda.Continua3.service.PrendasService;
import com.santamaria.tienda.Continua3.service.ColeccionesService;
import com.santamaria.tienda.Continua3.util.ConstantesApp;
import jakarta.validation.Valid;

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
		
		// Pasar los valores de filtro para mantenerlos en el formulario
		modelo.addAttribute("filtroColeccionId", coleccionId);
		modelo.addAttribute("filtroEstado", estado);
		modelo.addAttribute("filtroBusqueda", busqueda);
		
		return "prendas/lista";
	}

	@GetMapping("/registroPrenda")
	public String registroPrenda(Model modelo) {
		modelo.addAttribute("titulo", ConstantesApp.TITLE_PRENDAS_FORM);
		modelo.addAttribute("prenda", new PrendasEntity());
		modelo.addAttribute("colecciones", coleccionesServicio.listadoActivos());
		return "prendas/form";
	}

	@GetMapping("/editar/{id}")
	public String editarPrenda(@PathVariable Long id, Model modelo, RedirectAttributes redirectAttributes) {
		PrendasEntity prenda = prendasServicio.obtenerPorId(id).orElse(null);
		if (prenda == null) {
			redirectAttributes.addFlashAttribute("error", "Prenda no encontrada");
			return "redirect:/web/prendas/listar_prendas";
		}
		modelo.addAttribute("titulo", "Editar Prenda");
		modelo.addAttribute("prenda", prenda);
		modelo.addAttribute("colecciones", coleccionesServicio.listadoActivos());
		return "prendas/form";
	}

	@PostMapping("/guardar")
	public String guardar(@Valid @ModelAttribute PrendasEntity prenda, BindingResult result, 
			Model modelo, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			modelo.addAttribute("titulo", prenda.getIdPrenda() == null ? 
					ConstantesApp.TITLE_PRENDAS_FORM : "Editar Prenda");
			modelo.addAttribute("colecciones", coleccionesServicio.listadoActivos());
			return "prendas/form";
		}
		try {
			prendasServicio.guardar(prenda);
			redirectAttributes.addFlashAttribute("success", 
				prenda.getIdPrenda() == null ? "Prenda registrada exitosamente" : "Prenda actualizada exitosamente");
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
}
