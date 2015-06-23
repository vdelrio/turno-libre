package com.turnolibre.service;

import com.turnolibre.business.agenda.Agenda;
import com.turnolibre.business.prestador.PrestadorDeServicios;
import com.turnolibre.business.prestador.Servicio;

import java.util.List;
import java.util.Set;


public interface PrestadorDeServiciosService {

	List<PrestadorDeServicios> buscarPorServicioYCiudad(String servicio, String ciudad);

	PrestadorDeServicios buscarPorUrl(String url);

	Set<Servicio> buscarServicios(Long prestadorId);

	Set<Agenda> buscarAgendasPorServicio(Long prestadorId, Long servicioId);

}
