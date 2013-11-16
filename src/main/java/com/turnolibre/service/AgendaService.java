package com.turnolibre.service;

import com.turnolibre.business.agenda.DiaNoLaboral;
import com.turnolibre.business.agenda.JornadaLaboralHabitual;
import com.turnolibre.business.agenda.JornadaLaboralOcasional;
import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.turno.Horario;
import org.joda.time.Interval;

import java.util.Set;
import java.util.SortedSet;

public interface AgendaService {

	SortedSet<Horario> findHorarios(Long agendaId);

	SortedSet<Horario> findHorariosByInterval(Long agendaId, Interval intervalo);

	SortedSet<DiaNoLaboral> findDiasNoLaborales(Long agendaId);

	SortedSet<JornadaLaboralHabitual> findJornadasLaboralesHabituales(Long agendaId);

	Set<JornadaLaboralOcasional> findJornadasLaboralesOcasionales(Long agendaId);
	
	Long agregarJornadaLaboralHabitual(Long agendaId, JornadaLaboralHabitual jornadaHabitual) throws ExcepcionDeReglaDelNegocio;

	void quitarJornadaLaboralHabitual(Long agendaId, Long jornadaLaboralHabitualId, String motivo) throws ExcepcionDeReglaDelNegocio;
	
	Long agregarJornadaLaboralOcasional(Long agendaId, JornadaLaboralOcasional jornadaOcasional) throws ExcepcionDeReglaDelNegocio;

	void quitarJornadaLaboralOcasional(Long agendaId, Long jornadaLaboralOcasionalId, String motivo) throws ExcepcionDeReglaDelNegocio;
	
	Long agregarDiaNoLaboral(Long agendaId, DiaNoLaboral diaNoLaboral) throws ExcepcionDeReglaDelNegocio;
	
	void quitarDiaNoLaboral(Long agendaId, Long diaNoLaboralId) throws ExcepcionDeReglaDelNegocio;
	
	Long modificarDiaNoLaboral(Long agendaId, Long diaNoLaboralAModificarId, DiaNoLaboral diaNoLaboralModificado) throws ExcepcionDeReglaDelNegocio;
	
}
