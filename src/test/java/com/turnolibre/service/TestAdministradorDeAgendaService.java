package com.turnolibre.service;

import com.turnolibre.business.agenda.Agenda;
import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.usuario.AdministradorDeAgenda;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/business-config.xml", "classpath:spring/security-config.xml"})
@Transactional
@ActiveProfiles("test")
public class TestAdministradorDeAgendaService {

	@Autowired
	private AdministradorDeAgendaService administradorDeAgendaService;
	@Autowired
	private SharedService sharedService;

	
	@Test
	public void testFindById() throws ExcepcionDeReglaDelNegocio {

		AdministradorDeAgenda administradorDeAgenda = sharedService.load(AdministradorDeAgenda.class, 7L);
		assertNotNull(administradorDeAgenda);
		assertEquals("adm-agenda", administradorDeAgenda.getNombreDeRol());
		assertEquals("Admin Agenda1", administradorDeAgenda.getUsuario().getNombre());
	}

	@Test
	public void testFindAgendas() throws ExcepcionDeReglaDelNegocio {

		Collection<Agenda> agendas = administradorDeAgendaService.findAgendas(7L);
		assertNotNull(agendas);
		assertEquals(1, agendas.size());
	}
	
}
