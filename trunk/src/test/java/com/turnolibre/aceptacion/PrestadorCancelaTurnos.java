package com.turnolibre.aceptacion;

import com.turnolibre.business.turno.EstadoDeTurno;
import com.turnolibre.business.turno.Horario;
import com.turnolibre.business.turno.Turno;
import com.turnolibre.business.usuario.Notificacion;
import com.turnolibre.service.*;
import org.joda.time.DateTimeUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static util.TestConstants.*;


/**
 * [US-004] Como prestador de servicios quiero cancelar un turno
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/business-config.xml", "classpath:spring/security-config.xml"})
@TransactionConfiguration
@Transactional
@ActiveProfiles("test")
public class PrestadorCancelaTurnos {

	@Autowired
	private HorarioService horarioService;
	@Autowired
	private TurnoService turnoService;
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private UsuarioService usuarioService;
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
	 * [UAT-4.1] Cancelo todos los turnos de un horario con turnos tomados
	 * 
	 * DADO algún turno tomado de un horario
	 * CUANDO deshabilito todos los turnos de ese horario
	 * ENTONCES
	 * 	veo en la agenda que el horario esta deshabilitado Y
	 * 	veo un tooltip sobre el horario que me informa el motivo por el cual los turnos de ese horario estan deshabilitados Y
	 * 	se notifica a los clientes que tenían los turnos de ese horario Y
	 * 	se les deshabilita el turno a los clientes que tenían turno en el horario
	 */
	@Test
	public void cancelacionDeTodosConTurnosTomados() {
		
		Horario horario = sharedService.load(Horario.class, HORARIO_CON_TURNOS_TOMADOS_ID);
		List<Turno> turnosDeVictor = new ArrayList<Turno>(clienteService.findTurnos(CLIENTE_VICTOR_ID));
		Collection<Notificacion> notificacionesDeVictor = usuarioService.findNotificaciones(CLIENTE_VICTOR_ID);
		
		assertEquals(EstadoDeTurno.LIBRE, horario.getEstado()); // Porque le quedaba un turno libre
		assertFalse(MOTIVO_DE_DESHABILITACION.equals(horario.getComentarioDelEstado()));
		assertEquals(1, turnosDeVictor.size());
		assertEquals(EstadoDeTurno.OCUPADO, turnosDeVictor.get(0).getEstado());
		assertTrue(notificacionesDeVictor.isEmpty());
				
		horarioService.deshabilitarHorario(HORARIO_CON_TURNOS_TOMADOS_ID, MOTIVO_DE_DESHABILITACION);
		horario = sharedService.load(Horario.class, HORARIO_CON_TURNOS_TOMADOS_ID);
		turnosDeVictor = new ArrayList<Turno>(clienteService.findTurnos(CLIENTE_VICTOR_ID));
		notificacionesDeVictor = usuarioService.findNotificaciones(CLIENTE_VICTOR_ID);
		
		assertEquals(EstadoDeTurno.DESHABILITADO, horario.getEstado());
		assertEquals(MOTIVO_DE_DESHABILITACION, horario.getComentarioDelEstado());
		assertEquals(1, turnosDeVictor.size());
		assertEquals(EstadoDeTurno.DESHABILITADO, turnosDeVictor.get(0).getEstado());
		assertEquals(1, notificacionesDeVictor.size());
	}
	
	/**
	 * [UAT-4.2] Cancelo todos los turnos de un horario sin turnos tomados
	 * 
	 * DADO ningún turno tomado en un horario determinado
	 * CUANDO deshabilito todos los turnos de ese horario
	 * ENTONCES
	 * 	veo en la agenda que el horario esta deshabilitado Y
	 * 	veo un tooltip sobre el horario que me informa el motivo por el cual los turnos de ese horario estan deshabilitados
	 */
	@Test
	public void cancelacionDeTodosSinTurnosTomados() {
		
		Horario horario = sharedService.load(Horario.class, HORARIO_SIN_TURNOS_TOMADOS_ID);
		
		assertEquals(EstadoDeTurno.LIBRE, horario.getEstado());
		assertFalse(MOTIVO_DE_DESHABILITACION.equals(horario.getComentarioDelEstado()));
		
		horarioService.deshabilitarHorario(HORARIO_SIN_TURNOS_TOMADOS_ID, MOTIVO_DE_DESHABILITACION);
		horario = sharedService.load(Horario.class, HORARIO_SIN_TURNOS_TOMADOS_ID);
		
		assertEquals(EstadoDeTurno.DESHABILITADO, horario.getEstado());
		assertEquals(MOTIVO_DE_DESHABILITACION, horario.getComentarioDelEstado());
	}
	
	/**
	 * [UAT-4.3] Cancelo un turno tomado
	 * 
	 * DADO un turno tomado de un horario con más de un turno
	 * CUANDO deshabilito el turno
	 * ENTONCES
	 * 	veo en la agenda que el horario no ha cambiado de estado Y
	 * 	se le notifica al cliente que tenía el turno Y
	 * 	se le deshabilita el turno al cliente que tenía el turno 
	 */	
	@Test
	public void cancelacionDeUnTurnoTomado() {
		
		Horario horario = sharedService.load(Horario.class, HORARIO_SIN_TURNOS_TOMADOS_ID);
		Turno turnoDeVictor = sharedService.load(Turno.class, TURNO_DE_VICTOR_ID);
		Collection<Notificacion> notificacionesDeVictor = usuarioService.findNotificaciones(CLIENTE_VICTOR_ID);
		
		assertEquals(EstadoDeTurno.LIBRE, horario.getEstado());
		assertTrue(notificacionesDeVictor.isEmpty());
		assertEquals(EstadoDeTurno.OCUPADO, turnoDeVictor.getEstado());
		
		turnoService.deshabilitarTurno(TURNO_DE_VICTOR_ID, "Se rompio la maquina 2");
		horario = sharedService.load(Horario.class, HORARIO_SIN_TURNOS_TOMADOS_ID);
		turnoDeVictor = sharedService.load(Turno.class, TURNO_DE_VICTOR_ID);
		notificacionesDeVictor = usuarioService.findNotificaciones(CLIENTE_VICTOR_ID);
		
		assertEquals(EstadoDeTurno.LIBRE, horario.getEstado());
		assertEquals(1, notificacionesDeVictor.size());
		assertEquals(EstadoDeTurno.DESHABILITADO, turnoDeVictor.getEstado());
	}
	
}
