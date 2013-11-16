package com.turnolibre.service.impl;

import com.turnolibre.business.agenda.JornadaLaboralHabitual;
import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.joda.time.DayOfWeekTime;
import com.turnolibre.business.turno.Horario;
import com.turnolibre.persistence.dao.impl.SharedDao;
import com.turnolibre.service.JornadaLaboralHabitualService;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.SortedSet;

@Service
@Transactional
public class JornadaLaboralHabitualServiceImpl implements JornadaLaboralHabitualService {

	@Autowired
	private SharedDao sharedDao;


	/*------------------------------------ Public methods ----------------------------------*/

	@Override
	@Transactional(readOnly = true)
	public SortedSet<Horario> findHorarios(Long jornadaId) {

		JornadaLaboralHabitual jornadaHabitual = sharedDao.load(JornadaLaboralHabitual.class, jornadaId);
		return jornadaHabitual.getHorarios();
	}

	@Override
	@Transactional
	public void modificarJornada(Long jornadaId, JornadaLaboralHabitual jornadaHabitualModificada, String motivo) throws ExcepcionDeReglaDelNegocio {

		JornadaLaboralHabitual jornadaHabitual = sharedDao.load(JornadaLaboralHabitual.class, jornadaId);
		jornadaHabitual.modificarIntervalos(jornadaHabitualModificada.getDiaYHoraDeInicio(), jornadaHabitualModificada.getDiaYHoraDeFin(), motivo);
		jornadaHabitual.modificarDuracionDeLosTurnos(jornadaHabitualModificada.getDuracionDeLosTurnos());
		jornadaHabitual.modificarVacantesPorTurno(jornadaHabitualModificada.getVacantesPorTurno(), motivo);
	}

	@Override
	@Transactional
	public void modificarIntervalos(Long jornadaId, DayOfWeekTime nuevoDiaYHoraDeInicio, DayOfWeekTime nuevoDiaYHoraDeFin, String motivo) throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralHabitual jornadaHabitual = sharedDao.load(JornadaLaboralHabitual.class, jornadaId);
		jornadaHabitual.modificarIntervalos(nuevoDiaYHoraDeInicio, nuevoDiaYHoraDeFin, motivo);
	}

	@Override
	@Transactional
	public void modificarDuracionDeLosTurnos(Long jornadaId, Duration nuevaDuracion) throws ExcepcionDeReglaDelNegocio {

		JornadaLaboralHabitual jornadaHabitual = sharedDao.load(JornadaLaboralHabitual.class, jornadaId);
		jornadaHabitual.modificarDuracionDeLosTurnos(nuevaDuracion);
	}

	@Override
	@Transactional
	public void agregarVacantesPorTurno(Long jornadaId, Integer vacantesAAgregar) throws ExcepcionDeReglaDelNegocio {

		JornadaLaboralHabitual jornadaHabitual = sharedDao.load(JornadaLaboralHabitual.class, jornadaId);
		jornadaHabitual.agregarVacantesPorTurno(vacantesAAgregar);
	}

	@Override
	@Transactional
	public void quitarVacantesPorTurno(Long jornadaId, Integer vacantesAQuitar, String motivo) throws ExcepcionDeReglaDelNegocio {

		JornadaLaboralHabitual jornadaHabitual = sharedDao.load(JornadaLaboralHabitual.class, jornadaId);
		jornadaHabitual.quitarVacantesPorTurno(vacantesAQuitar, motivo);
	}

	/*--------------------------------------------------------------------------------------*/

}
