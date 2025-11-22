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
import com.santamaria.tienda.Continua3.service.ColeccionesService;
import com.santamaria.tienda.Continua3.util.ConstantesApp;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/web/coleccion")
public class ColeccionController {

	private final ColeccionesService servicio;

	public ColeccionController(ColeccionesService servicio) {
		this.servicio = servicio;
	}

	@GetMapping({"/listado", ""})
	public String coleccionListado(Model modelo) {
		modelo.addAttribute("titulo", ConstantesApp.TITLE_COLECCIONES_LISTA);
		modelo.addAttribute("listado", servicio.listado());
		return "colecciones/lista";
	}

	@GetMapping("/registroColeccion")
	public String registroColeccion(Model modelo) {
		modelo.addAttribute("titulo", ConstantesApp.TITLE_COLECCIONES_FORM);
		modelo.addAttribute("coleccion", new ColeccionesEntity());
		return "colecciones/form";
	}

	@GetMapping("/editar/{id}")
	public String editarColeccion(@PathVariable Long id, Model modelo, RedirectAttributes redirectAttributes) {
		ColeccionesEntity coleccion = servicio.buscarPorId(id).orElse(null);
		if (coleccion == null) {
			redirectAttributes.addFlashAttribute("error", "Colección no encontrada");
			return "redirect:/web/coleccion/listado";
		}
		modelo.addAttribute("titulo", "Editar Colección");
		modelo.addAttribute("coleccion", coleccion);
		return "colecciones/form";
	}

	@GetMapping("/detalle/{id}")
	public String detalleColeccion(@PathVariable Long id, Model modelo, RedirectAttributes redirectAttributes) {
		ColeccionesEntity coleccion = servicio.buscarPorId(id).orElse(null);
		if (coleccion == null) {
			redirectAttributes.addFlashAttribute("error", "Colección no encontrada");
			return "redirect:/web/coleccion/listado";
		}
		modelo.addAttribute("titulo", "Detalle de Colección");
		modelo.addAttribute("coleccion", coleccion);
		return "colecciones/detalle";
	}

	@PostMapping("/guardar")
	public String guardar(@Valid @ModelAttribute ColeccionesEntity coleccion, BindingResult result, 
			Model modelo, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			modelo.addAttribute("titulo", coleccion.getIdColeccion() == null ? 
					ConstantesApp.TITLE_COLECCIONES_FORM : "Editar Colección");
			return "colecciones/form";
		}
		try {
			servicio.guardar(coleccion);
			String mensaje = coleccion.getIdColeccion() == null ? 
				"¡Colección registrada exitosamente!" : "¡Colección actualizada exitosamente!";
			return "redirect:/web/coleccion/listado?success=" + java.net.URLEncoder.encode(mensaje, java.nio.charset.StandardCharsets.UTF_8);
		} catch (Exception e) {
			String mensajeError = "Error al guardar: " + e.getMessage();
			return "redirect:/web/coleccion/listado?error=" + java.net.URLEncoder.encode(mensajeError, java.nio.charset.StandardCharsets.UTF_8);
		}
	}

	@PostMapping("/inactivar/{id}")
	public String inactivar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			servicio.inactivar(id);
			String mensaje = "¡Colección inactivada exitosamente!";
			return "redirect:/web/coleccion/listado?success=" + java.net.URLEncoder.encode(mensaje, java.nio.charset.StandardCharsets.UTF_8);
		} catch (Exception e) {
			String mensajeError = "Error al inactivar: " + e.getMessage();
			return "redirect:/web/coleccion/listado?error=" + java.net.URLEncoder.encode(mensajeError, java.nio.charset.StandardCharsets.UTF_8);
		}
	}
}
