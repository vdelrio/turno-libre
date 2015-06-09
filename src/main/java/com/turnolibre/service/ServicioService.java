package com.turnolibre.service;

import com.turnolibre.business.prestador.PrestadorDeServicios;

import java.util.Set;


public interface ServicioService {

	Set<PrestadorDeServicios> buscarPrestadoresPorCiudad(String nombreServicio, String ciudad);

}
