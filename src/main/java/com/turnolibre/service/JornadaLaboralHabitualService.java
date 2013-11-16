package com.turnolibre.service;

import com.turnolibre.business.agenda.JornadaLaboralHabitual;
import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.joda.time.DayOfWeekTime;
import com.turnolibre.business.turno.Horario;
import org.joda.time.Duration;

import java.util.SortedSet;

public interface JornadaLaboralHabitualService {

	SortedSet<Horario> findHorarios(Long jornadaId);
	
	void modificarJornada(Long jornadaId, JornadaLaboralHabitual jornadaHabitualModificada, String motivo) throws ExcepcionDeReglaDelNegocio;
	
	void modificarIntervalos(Long jornadaId, DayOfWeekTime nuevoDiaYHoraDeInicio, DayOfWeekTime nuevoDiaYHoraDeFin, String motivo) throws ExcepcionDeReglaDelNegocio;
	
	void modificarDuracionDeLosTurnos(Long jornadaId, Duration nuevaDuracion) throws ExcepcionDeReglaDelNegocio;
	
	void agregarVacantesPorTurno(Long jornadaId, Integer vacantesAAgregar) throws ExcepcionDeReglaDelNegocio;

	void quitarVacantesPorTurno(Long jornadaId, Integer vacantesAQuitar, String motivo) throws ExcepcionDeReglaDelNegocio;
	
}
