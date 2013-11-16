package com.turnolibre.service.impl;

import com.turnolibre.business.agenda.Agenda;
import com.turnolibre.business.usuario.AdministradorDeAgenda;
import com.turnolibre.persistence.dao.impl.SharedDao;
import com.turnolibre.service.AdministradorDeAgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class AdministradorDeAgendaServiceImpl implements AdministradorDeAgendaService {

	@Autowired
	private SharedDao sharedDao;
	
	
	/*------------------------------------ Public methods ----------------------------------*/

	@Override
	@Transactional(readOnly = true)
	public Set<Agenda> findAgendas(Long admAgendaId) {

		AdministradorDeAgenda administradorDeAgenda = sharedDao.load(AdministradorDeAgenda.class, admAgendaId);
		return administradorDeAgenda.getAgendas();
	}
	
	/*--------------------------------------------------------------------------------------*/

}
