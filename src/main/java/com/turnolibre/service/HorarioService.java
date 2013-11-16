package com.turnolibre.service;

import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.turno.EstadoDeTurno;
import com.turnolibre.business.turno.Turno;

import java.util.SortedSet;

public interface HorarioService {

	Turno sacarTurno(Long horarioId, Long clienteId) throws ExcepcionDeReglaDelNegocio;

	SortedSet<Turno> findTurnos(Long horarioId);

	void deshabilitarHorario(Long horarioId, String motivo);

	EstadoDeTurno habilitarHorario(Long horarioId);

}
