package com.turnolibre.service.impl;

import com.turnolibre.business.prestador.PrestadorDeServicios;
import com.turnolibre.business.prestador.Rubro;
import com.turnolibre.persistence.dao.impl.SharedDao;
import com.turnolibre.service.RubroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class RubroServiceImpl implements RubroService {

	@Autowired
	private SharedDao sharedDao;


	/*------------------------------------ Public methods ----------------------------------*/

	@Override
	@Transactional(readOnly = true)
	public Set<PrestadorDeServicios> findPrestadores(Long rubroId) {

		Rubro rubro = sharedDao.load(Rubro.class, rubroId);
		return rubro.getPrestadoresDeServicio();
	}

	/*--------------------------------------------------------------------------------------*/

}
