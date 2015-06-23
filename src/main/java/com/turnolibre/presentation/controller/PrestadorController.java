package com.turnolibre.presentation.controller;

import com.turnolibre.business.prestador.PrestadorDeServicios;
import com.turnolibre.service.PrestadorDeServiciosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/prestador")
public class PrestadorController {

	@Autowired
	private PrestadorDeServiciosService prestadorDeServiciosService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return "prestador/index";
	}

	@RequestMapping(value = "buscar-por-servicio-ciudad", method = RequestMethod.GET)
	@ResponseBody
	public List<PrestadorDeServicios> buscarPorServicioYCiudad(String servicio, String ciudad) {
		return prestadorDeServiciosService.buscarPorServicioYCiudad(servicio, ciudad);
	}

	@RequestMapping(value = "{urlPrestador}/turnos", method = RequestMethod.GET)
	public String show(@PathVariable String urlPrestador, @RequestParam String servicio, Model model) {

		model.addAttribute("prestador", prestadorDeServiciosService.buscarPorUrl(urlPrestador));
		return "prestador/index";
	}

}