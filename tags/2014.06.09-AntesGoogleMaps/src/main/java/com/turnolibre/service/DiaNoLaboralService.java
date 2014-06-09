package com.turnolibre.service;

import com.turnolibre.business.turno.Horario;

import java.util.SortedSet;


public interface DiaNoLaboralService {

	SortedSet<Horario> findHorarios(Long diaNoLaboralId);
	
}
