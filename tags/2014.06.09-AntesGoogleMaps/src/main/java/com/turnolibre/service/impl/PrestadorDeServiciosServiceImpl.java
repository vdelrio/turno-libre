package com.turnolibre.service.impl;

import com.turnolibre.business.agenda.Agenda;
import com.turnolibre.business.prestador.PrestadorDeServicios;
import com.turnolibre.business.prestador.Servicio;
import com.turnolibre.persistence.dao.impl.SharedDao;
import com.turnolibre.service.PrestadorDeServiciosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class PrestadorDeServiciosServiceImpl implements PrestadorDeServiciosService {

	@Autowired
	private SharedDao sharedDao;


	/*------------------------------------ Public methods ----------------------------------*/

	@Override
	@Transactional(readOnly = true)
	public Set<Agenda> findAgendas(Long prestadorDeServiciosId) {

		PrestadorDeServicios prestadorDeServicios = sharedDao.load(PrestadorDeServicios.class, prestadorDeServiciosId);
		return prestadorDeServicios.getAgendas();
	}

	@Override
	@Transactional(readOnly = true)
	public Set<Servicio> findServicios(Long prestadorDeServiciosId) {

		PrestadorDeServicios prestadorDeServicios = sharedDao.load(PrestadorDeServicios.class, prestadorDeServiciosId);
		return prestadorDeServicios.getServicios();
	}

	/*--------------------------------------------------------------------------------------*/

}
