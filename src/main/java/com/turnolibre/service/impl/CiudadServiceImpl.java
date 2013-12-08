package com.turnolibre.service.impl;

import com.turnolibre.business.ubicacion.Ciudad;
import com.turnolibre.persistence.dao.impl.CiudadDao;
import com.turnolibre.service.CiudadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CiudadServiceImpl implements CiudadService {

	@Autowired
	private CiudadDao ciudadDao;


	@Override
	@Transactional(readOnly = true)
	public List<Ciudad> findByProvincia(Long provinciaId) {
		return ciudadDao.findByProvincia(provinciaId);
	}

}
