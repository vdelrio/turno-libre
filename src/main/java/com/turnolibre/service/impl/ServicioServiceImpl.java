package com.turnolibre.service.impl;

import com.turnolibre.business.prestador.PrestadorDeServicios;
import com.turnolibre.persistence.dao.impl.ServicioDao;
import com.turnolibre.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class ServicioServiceImpl implements ServicioService {

	@Autowired
	private ServicioDao servicioDao;


	/*------------------------------------ Public methods ----------------------------------*/

	@Override
	@Transactional(readOnly = true)
	public Set<PrestadorDeServicios> buscarPrestadoresPorCiudad(String nombreServicio, String ciudad) {
		return null;
	}

	/*--------------------------------------------------------------------------------------*/

}
