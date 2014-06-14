package com.turnolibre.service;

import com.turnolibre.business.agenda.Agenda;
import com.turnolibre.business.prestador.PrestadorDeServicios;
import com.turnolibre.business.prestador.Servicio;

import java.util.List;
import java.util.Set;


public interface PrestadorDeServiciosService {

	List<PrestadorDeServicios> findByCloseness(String serviceName, String address);

	Set<Agenda> findAgendas(Long prestadorDeServiciosId);

	Set<Servicio> findServicios(Long prestadorDeServiciosId);
	
}
