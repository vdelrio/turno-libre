package com.turnolibre.aceptacion;

import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.turno.EstadoDeTurno;
import com.turnolibre.business.turno.Horario;
import com.turnolibre.business.turno.Turno;
import com.turnolibre.service.ClienteService;
import com.turnolibre.service.HorarioService;
import com.turnolibre.service.SharedService;
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

import static org.junit.Assert.*;
import static util.TestConstants.*;


/**
 * [US-008] Como cliente quiero sacar turno
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/business-config.xml", "classpath:spring/security-config.xml"})
@Transactional
@ActiveProfiles("test")
public class ClienteSacaTurnos {

	@Autowired
	private HorarioService horarioService;
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private SharedService sharedService;

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DateTimeUtils.setCurrentMillisFixed(CURRENT_TIME_MOCK);
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		DateTimeUtils.setCurrentMillisSystem();
	}
	
	
	/**
	 * [UAT-8.1] Cliente saca turno 
	 * 
	 * DADO un horario con más de un turno libre 
	 * CUANDO saco el turno 
	 * ENTONCES 
	 *  veo el nuevo turno sacado en mis turnos Y 
	 *  veo que el horario sigue libre
	 */
	@Test
	public void clienteSacaTurno() throws ExcepcionDeReglaDelNegocio {
		
		// DADO un horario con más de un turno libre 
		Horario horario = sharedService.load(Horario.class, HORARIO_CON_MAS_DE_UN_TURNO_LIBRE_ID);
		Collection<Turno> turnosDeManuel = clienteService.findTurnos(CLIENTE_MANUEL_ID);
		
		assertEquals(EstadoDeTurno.LIBRE, horario.getEstado());
		assertEquals(1, turnosDeManuel.size());
		
		// CUANDO saco el turno 
		Turno turno = horarioService.sacarTurno(HORARIO_CON_MAS_DE_UN_TURNO_LIBRE_ID, CLIENTE_MANUEL_ID);
		
		horario = sharedService.load(Horario.class, HORARIO_CON_MAS_DE_UN_TURNO_LIBRE_ID);
		turnosDeManuel = clienteService.findTurnos(CLIENTE_MANUEL_ID);
		
		// ENTONCES veo el nuevo turno sacado en mis turnos
		assertEquals(2, turnosDeManuel.size());
		assertEquals(CLIENTE_MANUEL_ID, turno.getCliente().getId());
		
		// ENTONCES veo que el horario sigue libre
		assertEquals(EstadoDeTurno.LIBRE, horario.getEstado());
	}
	
	/**
	 * [UAT-8.2] Cliente saca el último turno 
	 * 
	 * DADO el último turno libre para un horario 
	 * CUANDO saco el turno 
	 * ENTONCES 
	 *  veo el nuevo turno sacado en mis turnos Y 
	 *  veo que el horario pasa a estar ocupado
	 */
	@Test
	public void clienteSacaUltimoTurno() throws ExcepcionDeReglaDelNegocio {
		
		Horario horario = sharedService.load(Horario.class, HORARIO_CON_ULTIMO_TURNO_LIBRE_ID);
		Collection<Turno> turnosDeJuan = clienteService.findTurnos(CLIENTE_JUAN_ID);
		
		assertEquals(EstadoDeTurno.LIBRE, horario.getEstado());
		assertTrue(turnosDeJuan.isEmpty());
		
		Turno turno = horarioService.sacarTurno(HORARIO_CON_ULTIMO_TURNO_LIBRE_ID, CLIENTE_JUAN_ID);
		horario = sharedService.load(Horario.class, HORARIO_CON_ULTIMO_TURNO_LIBRE_ID);
		turnosDeJuan = clienteService.findTurnos(CLIENTE_JUAN_ID);
		
		assertEquals(CLIENTE_JUAN_ID, turno.getCliente().getId());
		assertEquals(EstadoDeTurno.OCUPADO, horario.getEstado());
		assertEquals(1, turnosDeJuan.size());
	}
	
	/**
	 * [UAT-8.3] Cliente intenta sacar turno en horario para el cual ya tiene turno 
	 * 
	 * DADO un turno libre en un un horario en el cual ya saque un turno 
	 * CUANDO intento sacar otro turno en ese horario 
	 * ENTONCES el sistema no me deja realizar la operación
	 */
	@Test(expected=ExcepcionDeReglaDelNegocio.class)
	public void clienteSacaTurnoParaHorarioEnElCualYaTieneTurno() throws ExcepcionDeReglaDelNegocio {
		horarioService.sacarTurno(HORARIO_CON_ULTIMO_TURNO_LIBRE_ID, CLIENTE_VICTOR_ID);
	}
	
	/**
	 * [UAT-8.4] Cliente inteta sacar turno en un horario ocupado 
	 * 
	 * DADO un horario con todos sus turnos ocupados 
	 * CUANDO intento sacar turno para dicho horario 
	 * ENTONCES el sistema no me deja realizar la operación 
	 */
	@Test
	public void clienteSacaTurnoOcupado() throws ExcepcionDeReglaDelNegocio {
		
		horarioService.sacarTurno(HORARIO_CON_ULTIMO_TURNO_LIBRE_ID, CLIENTE_CARLOS_ID);
		Turno turno = horarioService.sacarTurno(HORARIO_CON_ULTIMO_TURNO_LIBRE_ID, CLIENTE_JUAN_ID);
		assertNull(turno);
	}
	
	/**
	 * [UAT-8.5] Cliente inteta sacar turno en un horario deshabilitado 
	 * 
	 * DADO un horario con todos sus turnos deshabilitados 
	 * CUANDO intento sacar turno para dicho horario 
	 * ENTONCES el sistema no me deja realizar la operación 
	 */
	@Test
	public void clienteSacaTurnoDeshabilitado() throws ExcepcionDeReglaDelNegocio {
		
		horarioService.deshabilitarHorario(HORARIO_CON_ULTIMO_TURNO_LIBRE_ID, "Urgencia familiar");
		Turno turno = horarioService.sacarTurno(HORARIO_CON_ULTIMO_TURNO_LIBRE_ID, CLIENTE_JUAN_ID);
		assertNull(turno);
	}
	
}
