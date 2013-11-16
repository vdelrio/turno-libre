package com.turnolibre.service.impl;

import com.turnolibre.business.turno.Turno;
import com.turnolibre.business.usuario.Cliente;
import com.turnolibre.persistence.dao.impl.SharedDao;
import com.turnolibre.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.SortedSet;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	private SharedDao sharedDao;


	/*------------------------------------ Public methods ----------------------------------*/

	@Override
	@Transactional(readOnly = true)
	public SortedSet<Turno> findTurnos(Long clienteId) {

		Cliente cliente = sharedDao.load(Cliente.class, clienteId);
		return cliente.getTurnos();
	}

	/*--------------------------------------------------------------------------------------*/

}
