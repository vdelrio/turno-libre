package com.turnolibre.service.impl;

import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.turno.EstadoDeTurno;
import com.turnolibre.business.turno.Turno;
import com.turnolibre.business.usuario.Cliente;
import com.turnolibre.persistence.dao.impl.SharedDao;
import com.turnolibre.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TurnoServiceImpl implements TurnoService {

	@Autowired
	private SharedDao sharedDao;


	/*------------------------------------ Public methods ----------------------------------*/

	@Override
	@Transactional
	public void cancelarTurno(Long turnoId, Long clienteId) throws ExcepcionDeReglaDelNegocio {

		Turno turno = sharedDao.load(Turno.class, turnoId);
		Cliente cliente = sharedDao.load(Cliente.class, clienteId);

		turno.cancelar(cliente);
	}

	@Override
	@Transactional
	public void deshabilitarTurno(Long turnoId, String motivo) {

		Turno turno = sharedDao.load(Turno.class, turnoId);
		turno.deshabilitar(motivo);
	}

	@Override
	@Transactional
	public EstadoDeTurno habilitarTurno(Long turnoId) {

		Turno turno = sharedDao.load(Turno.class, turnoId);
		turno.habilitar();

		return turno.getEstado();
	}

	/*--------------------------------------------------------------------------------------*/

}
