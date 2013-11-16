package com.turnolibre.service;

import com.turnolibre.business.agenda.Agenda;

import java.util.Set;


public interface AdministradorDeAgendaService {

	Set<Agenda> findAgendas(Long admAgendaId);
	
}
