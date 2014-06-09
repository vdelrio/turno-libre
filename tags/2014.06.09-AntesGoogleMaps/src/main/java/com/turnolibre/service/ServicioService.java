package com.turnolibre.service;

import com.turnolibre.business.agenda.Agenda;

import java.util.Set;


public interface ServicioService {

	Set<Agenda> findAgendas(Long servicioId);

}
