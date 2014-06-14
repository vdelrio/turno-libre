package com.turnolibre.service.impl;

import com.turnolibre.business.agenda.Agenda;
import com.turnolibre.business.prestador.PrestadorDeServicios;
import com.turnolibre.business.prestador.Servicio;
import com.turnolibre.business.ubicacion.Ubicacion;
import com.turnolibre.persistence.dao.impl.SharedDao;
import com.turnolibre.service.PrestadorDeServiciosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PrestadorDeServiciosServiceImpl implements PrestadorDeServiciosService {

	@Autowired
	private SharedDao sharedDao;


	/*------------------------------------ Public methods ----------------------------------*/

	@Override
	public List<PrestadorDeServicios> findByCloseness(String serviceName, String address) {
		// TODO desharcodear esta lista
		return Arrays.asList(
				new PrestadorDeServicios("Prestador 1", new Ubicacion("Aguilar 2345, Buenos Aires", -34.5685359,-58.4467496), "12345"),
				new PrestadorDeServicios("Prestador 2", new Ubicacion("Ciudad de la Paz 1032, Buenos Aires", -34.5700622,-58.4485296), "12345"),
				new PrestadorDeServicios("Prestador 3", new Ubicacion("Av Cabildo 1112, Buenos Aires", -34.5688308,-58.4471497), "12345"),
				new PrestadorDeServicios("Prestador 4", new Ubicacion("Palpa 2653, Buenos Aires", -34.5716346,-58.4484501), "12345")
		);
	}

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
