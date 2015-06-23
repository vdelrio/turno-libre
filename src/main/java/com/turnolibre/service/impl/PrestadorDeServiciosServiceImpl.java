package com.turnolibre.service.impl;

import com.turnolibre.business.agenda.Agenda;
import com.turnolibre.business.prestador.PrestadorDeServicios;
import com.turnolibre.business.prestador.Servicio;
import com.turnolibre.persistence.dao.impl.PrestadorDeServiciosDao;
import com.turnolibre.persistence.dao.impl.ServicioDao;
import com.turnolibre.persistence.dao.impl.SharedDao;
import com.turnolibre.service.PrestadorDeServiciosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PrestadorDeServiciosServiceImpl implements PrestadorDeServiciosService {

	@Autowired
	private SharedDao sharedDao;
	@Autowired
	private ServicioDao servicioDao;
	@Autowired
	private PrestadorDeServiciosDao prestadorDeServiciosDao;


	/*------------------------------------ Public methods ----------------------------------*/

	@Override
	@Transactional(readOnly = true)
	public List<PrestadorDeServicios> buscarPorServicioYCiudad(String nombreServicio, String ciudad) {

		Servicio servicio = servicioDao.buscarPorNombre(nombreServicio);
		return prestadorDeServiciosDao.buscarPorServicioYCiudad(servicio, ciudad);
	}

	@Override
	public PrestadorDeServicios buscarPorUrl(String url) {
		return prestadorDeServiciosDao.buscarPorUrl(url);
	}

	@Override
	@Transactional(readOnly = true)
	public Set<Servicio> buscarServicios(Long prestadorId) {

		PrestadorDeServicios prestadorDeServicios = sharedDao.load(PrestadorDeServicios.class, prestadorId);
		return prestadorDeServicios.getServicios();
	}

	@Override
	@Transactional(readOnly = true)
	public Set<Agenda> buscarAgendasPorServicio(Long prestadorId, Long servicioId) {

		PrestadorDeServicios prestador = sharedDao.load(PrestadorDeServicios.class, prestadorId);
		Servicio servicio = sharedDao.load(Servicio.class, servicioId);

		return prestador.getAgendas(servicio);
	}

	/*--------------------------------------------------------------------------------------*/

}
