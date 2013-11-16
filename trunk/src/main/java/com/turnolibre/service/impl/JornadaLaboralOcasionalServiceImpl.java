package com.turnolibre.service.impl;

import com.turnolibre.business.agenda.JornadaLaboralOcasional;
import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.i18n.MensajeLocalizable;
import com.turnolibre.business.turno.Horario;
import com.turnolibre.persistence.dao.impl.SharedDao;
import com.turnolibre.service.JornadaLaboralOcasionalService;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.SortedSet;

@Service
@Transactional
public class JornadaLaboralOcasionalServiceImpl implements JornadaLaboralOcasionalService {

	@Autowired
	private SharedDao sharedDao;


	/*------------------------------------ Public methods ----------------------------------*/

	@Override
	@Transactional(readOnly = true)
	public SortedSet<Horario> findHorarios(Long jornadaId) {

		JornadaLaboralOcasional jornadaLaboralOcasional = sharedDao.load(JornadaLaboralOcasional.class, jornadaId);
		return jornadaLaboralOcasional.getHorarios();
	}

	@Override
	@Transactional
	public void modificarJornada(Long jornadaId, JornadaLaboralOcasional jornadaOcasionalModificada, String motivo) throws ExcepcionDeReglaDelNegocio {

		try {

			Interval intervalo = jornadaOcasionalModificada.getIntervalo();

			JornadaLaboralOcasional jornadaOcasional = sharedDao.load(JornadaLaboralOcasional.class, jornadaId);
			jornadaOcasional.modificarIntervalo(intervalo, motivo);
			jornadaOcasional.modificarDuracionDeLosTurnos(jornadaOcasionalModificada.getDuracionDeLosTurnos());
			jornadaOcasional.modificarVacantesPorTurno(jornadaOcasionalModificada.getVacantesPorTurno(), motivo);

		} catch (IllegalArgumentException e) {
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.modificar.jornada.intervalo.mal.formado"));
		}
	}

	@Override
	@Transactional
	public void modificarIntervalo(Long jornadaId, Interval nuevoIntervalo, String motivo) throws ExcepcionDeReglaDelNegocio {

		JornadaLaboralOcasional jornadaOcasional = sharedDao.load(JornadaLaboralOcasional.class, jornadaId);
		jornadaOcasional.modificarIntervalo(nuevoIntervalo, motivo);
	}

	@Override
	@Transactional
	public void modificarDuracionDeLosTurnos(Long jornadaId, Duration nuevaDuracion) throws ExcepcionDeReglaDelNegocio {

		JornadaLaboralOcasional jornadaOcasional = sharedDao.load(JornadaLaboralOcasional.class, jornadaId);
		jornadaOcasional.modificarDuracionDeLosTurnos(nuevaDuracion);
	}

	@Override
	@Transactional
	public void agregarVacantesPorTurno(Long jornadaId, Integer vacantesAAgregar) throws ExcepcionDeReglaDelNegocio {

		JornadaLaboralOcasional jornadaOcasional = sharedDao.load(JornadaLaboralOcasional.class, jornadaId);
		jornadaOcasional.agregarVacantesPorTurno(vacantesAAgregar);
	}

	@Override
	@Transactional
	public void quitarVacantesPorTurno(Long jornadaId, Integer vacantesAQuitar, String motivo) throws ExcepcionDeReglaDelNegocio {

		JornadaLaboralOcasional jornadaOcasional = sharedDao.load(JornadaLaboralOcasional.class, jornadaId);
		jornadaOcasional.quitarVacantesPorTurno(vacantesAQuitar, motivo);
	}

	/*--------------------------------------------------------------------------------------*/

}
