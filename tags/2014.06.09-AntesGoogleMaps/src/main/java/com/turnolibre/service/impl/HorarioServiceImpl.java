package com.turnolibre.service.impl;

import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.turno.EstadoDeTurno;
import com.turnolibre.business.turno.Horario;
import com.turnolibre.business.turno.Turno;
import com.turnolibre.business.usuario.Cliente;
import com.turnolibre.persistence.dao.impl.SharedDao;
import com.turnolibre.service.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.SortedSet;

@Service
@Transactional
public class HorarioServiceImpl implements HorarioService {

	@Autowired
	private SharedDao sharedDao;


	/*------------------------------------ Public methods ----------------------------------*/

	@Override
	@Transactional
	public Turno sacarTurno(Long horarioId, Long clienteId) throws ExcepcionDeReglaDelNegocio {

		Horario horario = sharedDao.load(Horario.class, horarioId);
		Cliente cliente = sharedDao.load(Cliente.class, clienteId);

		return horario.sacarTurno(cliente);
	}

	@Override
	@Transactional(readOnly = true)
	public SortedSet<Turno> findTurnos(Long horarioId) {

		Horario horario = sharedDao.load(Horario.class, horarioId);
		return horario.getTurnos();
	}

	@Override
	@Transactional
	public void deshabilitarHorario(Long horarioId, String motivo) {

		Horario horario = sharedDao.load(Horario.class, horarioId);
		horario.deshabilitarTurnos(motivo);
	}

	@Override
	@Transactional
	public EstadoDeTurno habilitarHorario(Long horarioId) {

		Horario horario = sharedDao.load(Horario.class, horarioId);
		horario.habilitarTurnos();

		return horario.getEstado();
	}

	/*--------------------------------------------------------------------------------------*/

}
