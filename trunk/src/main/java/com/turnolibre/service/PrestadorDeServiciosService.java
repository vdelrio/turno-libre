package com.turnolibre.service;

import com.turnolibre.business.agenda.Agenda;
import com.turnolibre.business.prestador.Servicio;

import java.util.Set;


public interface PrestadorDeServiciosService {

	Set<Agenda> findAgendas(Long prestadorDeServiciosId);

	Set<Servicio> findServicios(Long prestadorDeServiciosId);
	
}
