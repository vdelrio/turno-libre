package com.turnolibre.presentation.controller;

import com.turnolibre.business.prestador.PrestadorDeServicios;
import com.turnolibre.service.PrestadorDeServiciosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

	@Autowired
	private PrestadorDeServiciosService prestadorDeServiciosService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return "home/index";
	}

	@RequestMapping(value = "buscar-por-servicio-ciudad", method = RequestMethod.GET)
	@ResponseBody
	public List<PrestadorDeServicios> buscarPorServicioYCiudad(String servicio, String ciudad) {
		return prestadorDeServiciosService.buscarPorServicioYCiudad(servicio, ciudad);
	}

}