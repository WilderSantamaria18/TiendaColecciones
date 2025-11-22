package com.santamaria.tienda.Continua3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.santamaria.tienda.Continua3.util.ConstantesApp;

@Controller
public class InicioController {

	@GetMapping("/")
	public String home(Model modelo) {
	
		modelo.addAttribute("appAutor", ConstantesApp.APP_AUTOR);
		modelo.addAttribute("appCiclo", ConstantesApp.APP_CICLO);
		modelo.addAttribute("appInstitucion", ConstantesApp.APP_INSTITUCION);
		modelo.addAttribute("appVersion", ConstantesApp.APP_VERSION);
		return "index";
	}
}
