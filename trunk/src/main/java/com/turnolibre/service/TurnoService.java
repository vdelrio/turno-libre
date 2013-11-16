package com.turnolibre.service;

import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.turno.EstadoDeTurno;

public interface TurnoService {

	void cancelarTurno(Long turnoId, Long clienteId) throws ExcepcionDeReglaDelNegocio;
	
	void deshabilitarTurno(Long turnoId, String motivo);

	EstadoDeTurno habilitarTurno(Long turnoId);
	
}
