package com.turnolibre.aceptacion;

import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.turno.Turno;
import com.turnolibre.business.usuario.Notificacion;
import com.turnolibre.service.ClienteService;
import com.turnolibre.service.TurnoService;
import com.turnolibre.service.UsuarioService;
import org.joda.time.DateTimeUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static util.TestConstants.*;


/**
 * [US-010] Como cliente quiero cancelar un turno
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/business-config.xml", "classpath:spring/security-config.xml"})
@Transactional
@ActiveProfiles("test")
public class ClienteCancelaTurnos {

	@Autowired
	private TurnoService turnoService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private ClienteService clienteService;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DateTimeUtils.setCurrentMillisFixed(CURRENT_TIME_MOCK);
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		DateTimeUtils.setCurrentMillisSystem();
	}
	
	
	/**
	 * [UAT-10.1] Cliente cancela turno
	 * 
	 * DADO un turno sacado por mi como cliente
	 * CUANDO cancelo el turno
	 * ENTONCES 
	 *  veo que el turno ya no esta en mis turnos Y 
	 *  se le notifico a los administradores de la agenda que cancele el turno
	 */
	@Test
	public void clienteCancelaTurno() throws ExcepcionDeReglaDelNegocio {

		Collection<Turno> turnosDeVictor = clienteService.findTurnos(CLIENTE_VICTOR_ID);
		Collection<Notificacion> notificacionesDelAdmAgenda1 = usuarioService.findNotificaciones(ADM_DE_AGENDA_2_ID);
		
		assertEquals(1, turnosDeVictor.size());
		assertTrue(notificacionesDelAdmAgenda1.isEmpty());
		
		turnoService.cancelarTurno(TURNO_DE_VICTOR_ID, CLIENTE_VICTOR_ID);
		turnosDeVictor = clienteService.findTurnos(CLIENTE_VICTOR_ID);
		notificacionesDelAdmAgenda1 = usuarioService.findNotificaciones(ADM_DE_AGENDA_2_ID);
		
		assertTrue(turnosDeVictor.isEmpty());
		assertEquals(1, notificacionesDelAdmAgenda1.size());
	}
	
}
