package com.turnolibre.service.impl;

import com.turnolibre.business.agenda.DiaNoLaboral;
import com.turnolibre.business.turno.Horario;
import com.turnolibre.persistence.dao.impl.SharedDao;
import com.turnolibre.service.DiaNoLaboralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.SortedSet;

@Service
@Transactional
public class DiaNoLaboralServiceImpl implements DiaNoLaboralService {

	@Autowired
	private SharedDao sharedDao;


	/*------------------------------------ Public methods ----------------------------------*/

	@Override
	@Transactional(readOnly = true)
	public SortedSet<Horario> findHorarios(Long diaNoLaboralId) {

		DiaNoLaboral diaNoLaboral = sharedDao.load(DiaNoLaboral.class, diaNoLaboralId);
		return diaNoLaboral.getHorarios();
	}

	/*--------------------------------------------------------------------------------------*/

}
