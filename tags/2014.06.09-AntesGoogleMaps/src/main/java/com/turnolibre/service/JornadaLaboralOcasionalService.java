package com.turnolibre.service;

import com.turnolibre.business.agenda.JornadaLaboralOcasional;
import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.turno.Horario;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.util.SortedSet;

public interface JornadaLaboralOcasionalService {

	SortedSet<Horario> findHorarios(Long jornadaId);
	
	void modificarJornada(Long jornadaId, JornadaLaboralOcasional jornadaOcasionalModificada, String motivo) throws ExcepcionDeReglaDelNegocio;
	
	void modificarIntervalo(Long jornadaId, Interval nuevoIntervalo, String motivo) throws ExcepcionDeReglaDelNegocio;
	
	void modificarDuracionDeLosTurnos(Long jornadaId, Duration nuevaDuracion) throws ExcepcionDeReglaDelNegocio;
	
	void agregarVacantesPorTurno(Long jornadaId, Integer vacantesAAgregar) throws ExcepcionDeReglaDelNegocio;

	void quitarVacantesPorTurno(Long jornadaId, Integer vacantesAQuitar, String motivo) throws ExcepcionDeReglaDelNegocio;
	
}
