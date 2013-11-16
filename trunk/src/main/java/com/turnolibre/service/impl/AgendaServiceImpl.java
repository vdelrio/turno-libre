package com.turnolibre.service.impl;

import com.turnolibre.business.agenda.*;
import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.i18n.MensajeLocalizable;
import com.turnolibre.business.turno.Horario;
import com.turnolibre.persistence.dao.impl.SharedDao;
import com.turnolibre.service.AgendaService;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.SortedSet;

@Service
@Transactional
public class AgendaServiceImpl implements AgendaService {


	@Autowired
	private SharedDao sharedDao;

	/*------------------------------------ Public methods ----------------------------------*/

	@Override
	@Transactional(readOnly = true)
	public SortedSet<Horario> findHorarios(Long agendaId) {

		Agenda agenda = sharedDao.load(Agenda.class, agendaId);
		return agenda.getHorarios();
	}

	@Override
	@Transactional(readOnly = true)
	public SortedSet<Horario> findHorariosByInterval(Long agendaId, Interval intervalo) {

		Agenda agenda = sharedDao.load(Agenda.class, agendaId);
		return FiltroDeHorarios.ejecutar(agenda.getHorarios(), intervalo);
	}

	@Override
	@Transactional(readOnly = true)
	public SortedSet<DiaNoLaboral> findDiasNoLaborales(Long agendaId) {

		Agenda agenda = sharedDao.load(Agenda.class, agendaId);
		return agenda.getDiasNoLaborales();
	}

	@Override
	@Transactional(readOnly = true)
	public SortedSet<JornadaLaboralHabitual> findJornadasLaboralesHabituales(Long agendaId) {

		Agenda agenda = sharedDao.load(Agenda.class, agendaId);
		return agenda.getJornadasLaboralesHabituales();
	}

	@Override
	@Transactional(readOnly = true)
	public Set<JornadaLaboralOcasional> findJornadasLaboralesOcasionales(Long agendaId) {

		Agenda agenda = sharedDao.load(Agenda.class, agendaId);
		return agenda.getJornadasLaboralesOcasionales();
	}

	@Override
	@Transactional
	public Long agregarJornadaLaboralHabitual(Long agendaId, JornadaLaboralHabitual jornadaHabitual) throws ExcepcionDeReglaDelNegocio {

		Agenda agenda = sharedDao.load(Agenda.class, agendaId);
		agenda.agregarJornadaLaboralHabitual(jornadaHabitual);

		sharedDao.saveOrUpdate(jornadaHabitual);
		return jornadaHabitual.getId();
	}

	@Override
	@Transactional
	public void quitarJornadaLaboralHabitual(Long agendaId, Long jornadaLaboralHabitualId, String motivo) throws ExcepcionDeReglaDelNegocio {

		Agenda agenda = sharedDao.load(Agenda.class, agendaId);
		JornadaLaboralHabitual jornadaHabitual = sharedDao.load(JornadaLaboralHabitual.class, jornadaLaboralHabitualId);
		agenda.quitarJornadaLaboralHabitual(jornadaHabitual, motivo);
	}

	@Override
	@Transactional
	public Long agregarJornadaLaboralOcasional(Long agendaId, JornadaLaboralOcasional jornadaOcasional) throws ExcepcionDeReglaDelNegocio {

		Agenda agenda = sharedDao.load(Agenda.class, agendaId);
		agenda.agregarJornadaLaboralOcasional(jornadaOcasional);

		sharedDao.saveOrUpdate(jornadaOcasional);
		return jornadaOcasional.getId();
	}

	@Override
	@Transactional
	public void quitarJornadaLaboralOcasional(Long agendaId, Long jornadaLaboralOcasionalId, String motivo) throws ExcepcionDeReglaDelNegocio {

		Agenda agenda = sharedDao.load(Agenda.class, agendaId);
		JornadaLaboralOcasional jornadaOcasional = sharedDao.load(JornadaLaboralOcasional.class, jornadaLaboralOcasionalId);
		agenda.quitarJornadaLaboralOcasional(jornadaOcasional, motivo);
	}

	@Override
	@Transactional
	public Long agregarDiaNoLaboral(Long agendaId, DiaNoLaboral diaNoLaboral) throws ExcepcionDeReglaDelNegocio {

		Agenda agenda = sharedDao.load(Agenda.class, agendaId);
		agenda.agregarDiaNoLaboral(diaNoLaboral);

		sharedDao.saveOrUpdate(diaNoLaboral);
		return diaNoLaboral.getId();
	}

	@Override
	@Transactional
	public void quitarDiaNoLaboral(Long agendaId, Long diaNoLaboralId) throws ExcepcionDeReglaDelNegocio {

		Agenda agenda = sharedDao.load(Agenda.class, agendaId);
		DiaNoLaboral diaNoLaboral = sharedDao.load(DiaNoLaboral.class, diaNoLaboralId);
		agenda.quitarDiaNoLaboral(diaNoLaboral);
	}

	@Override
	@Transactional
	public Long modificarDiaNoLaboral(Long agendaId, Long diaNoLaboralAModificarId, DiaNoLaboral diaNoLaboralModificado) throws ExcepcionDeReglaDelNegocio {

		Agenda agenda = sharedDao.load(Agenda.class, agendaId);
		DiaNoLaboral diaNoLaboralAModificar = sharedDao.load(DiaNoLaboral.class, diaNoLaboralAModificarId);

		agenda.quitarDiaNoLaboral(diaNoLaboralAModificar);

		try {
			agenda.agregarDiaNoLaboral(diaNoLaboralModificado);

			sharedDao.saveOrUpdate(diaNoLaboralModificado);
			return diaNoLaboralModificado.getId();

		} catch (ExcepcionDeReglaDelNegocio e) {
			agenda.agregarDiaNoLaboral(diaNoLaboralAModificar);
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.modificar.dia.no.laboral.dia.repetido"));
		}
	}

	/*--------------------------------------------------------------------------------------*/

}
