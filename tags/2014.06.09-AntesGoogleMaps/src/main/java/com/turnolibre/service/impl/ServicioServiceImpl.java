package com.turnolibre.service.impl;

import com.turnolibre.business.agenda.Agenda;
import com.turnolibre.business.prestador.Servicio;
import com.turnolibre.persistence.dao.impl.SharedDao;
import com.turnolibre.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class ServicioServiceImpl implements ServicioService {

	@Autowired
	private SharedDao sharedDao;


	/*------------------------------------ Public methods ----------------------------------*/

	@Override
	@Transactional(readOnly = true)
	public Set<Agenda> findAgendas(Long servicioId) {

		Servicio servicio = sharedDao.load(Servicio.class, servicioId);
		return servicio.getAgendas();
	}

	/*--------------------------------------------------------------------------------------*/

}
