package com.turnolibre.service.impl;

import com.turnolibre.business.prestador.Servicio;
import com.turnolibre.persistence.dao.impl.ServicioDao;
import com.turnolibre.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioServiceImpl implements ServicioService {

	@Autowired
	private ServicioDao servicioDao;


	/*------------------------------------ Public methods ----------------------------------*/

	@Override
	@Transactional(readOnly = true)
	public Servicio buscarPorNombre(String nombre) {
		return servicioDao.buscarPorNombre(nombre);
	}

	/*--------------------------------------------------------------------------------------*/

}
