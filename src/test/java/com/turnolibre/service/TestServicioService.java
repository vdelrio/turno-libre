package com.turnolibre.service;

import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.prestador.Servicio;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/business-config.xml", "classpath:spring/security-config.xml"})
@Transactional
@ActiveProfiles("test")
public class TestServicioService {

	@Autowired
	private ServicioService servicioService;

	
	@Test
	public void testBuscarPorNombreExistente() throws ExcepcionDeReglaDelNegocio {

		Servicio servicioExistente = servicioService.buscarPorNombre("Oftalmología");
		assertNotNull(servicioExistente);
		assertEquals("Oftalmología", servicioExistente.getNombre());
	}

	@Test
	public void testBuscarPorNombreInexistente() throws ExcepcionDeReglaDelNegocio {

		Servicio servicioInexistente = servicioService.buscarPorNombre("ServicioFalso");
		assertNull(servicioInexistente);
	}

}
