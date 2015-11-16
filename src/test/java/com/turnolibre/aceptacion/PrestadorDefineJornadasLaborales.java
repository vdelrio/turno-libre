package com.turnolibre.aceptacion;

import com.turnolibre.business.agenda.JornadaLaboralHabitual;
import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static org.junit.Assert.*;
import static util.TestConstants.*;

/**
 * [US-001] Como prestador de servicios quiero definir mis jornadas laborales
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/business-config.xml", "classpath:spring/security-config.xml"})
@Transactional
@ActiveProfiles("test")
public class PrestadorDefineJornadasLaborales {

	@Autowired
	private AgendaService agendaService;
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private DiaNoLaboralService diaNoLaboralService;
	@Autowired
	private JornadaLaboralHabitualService jornadaLaboralHabitualService;
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
	 * [UAT-1.1] Alta de jornada laboral sin problemas
	 * DADA alguna jornada laboral ya cargada para la agenda
	 * CUANDO agrego una nueva jornada laboral con todos los campos correctos y que no se superpone con las ya cargadas
	 * ENTONCES
	 * 	veo en la agenda la nueva jornada laboral para la fecha actual en adelante Y
	 * 	veo que los horarios tienen la duración que defini Y 
	 * 	veo que los horarios tienen la cantidad de turnos que defini Y 
	 * 	veo que los turnos estan disponibles 
	 */
	@Test
	public void altaDeJornadaLaboralSinProblemas() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralHabitual jornadaHabitual = new JornadaLaboralHabitual(MARTES_A_LAS_09, MARTES_A_LAS_11, _1_HORA, _2_TURNOS);
		Long jornadaId = agendaService.agregarJornadaLaboralHabitual(AGENDA_DRA_LOPEZ_ID, jornadaHabitual);

		Collection<Horario> horariosDeLaJornada = jornadaLaboralHabitualService.findHorarios(jornadaId);
		
		// veo en la agenda la nueva jornada laboral para la fecha actual en adelante
		assertFalse(horariosDeLaJornada.isEmpty());
		
		for (Horario horario : horariosDeLaJornada) {
			
			// veo que los horarios tienen la duración que defini
			assertEquals(_1_HORA, horario.getIntervalo().toDuration());
			
			// veo que los horarios tienen la cantidad de turnos que defini
			assertEquals(_2_TURNOS, horario.getTurnos().size());
			
			// veo que los turnos estan disponibles
			for (Turno turno : horario.getTurnos()) {
				assertEquals(EstadoDeTurno.LIBRE, turno.getEstado());
			}
		}
	}
	 
	/**
	 * [UAT-1.2] Alta de jornada laboral con intervalo que no es posible por duración de los turnos
	 * DADA una agenda
	 * CUANDO intento agregar en ella una jornada laboral cuya duracion de su intervalo no es posible conforme a la duración de los turnos elegida
	 * ENTONCES el sistema no me deja realizar la operación
	 */
	@Test(expected=ExcepcionDeReglaDelNegocio.class)
	public void altaDeJornadaLaboralConIntervaloQueNoEsPosiblePorDuracionDeLosTurnos() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralHabitual jornadaHabitual = new JornadaLaboralHabitual(MARTES_A_LAS_09, MARTES_A_LAS_11, _1_30_HORAS, _2_TURNOS);
		agendaService.agregarJornadaLaboralHabitual(AGENDA_DRA_LOPEZ_ID, jornadaHabitual);
	}
	 
	/**
	 * [UAT-1.3] Alta de jornada laboral superpuesta
	 * DADA alguna jornada laboral ya cargada  para la agenda
	 * CUANDO intento agregar una jornada laboral que se superpone con alguna existente
	 * ENTONCES el sistema no me deja realizar la operación
	 */
	@Test(expected=ExcepcionDeReglaDelNegocio.class)
	public void altaDeJornadaLaboralSuperpuesta() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralHabitual jornadaHabitual = new JornadaLaboralHabitual(LUNES_A_LAS_09, LUNES_A_LAS_15, _1_30_HORAS, _2_TURNOS);
		agendaService.agregarJornadaLaboralHabitual(AGENDA_DRA_LOPEZ_ID, jornadaHabitual);
	}
	
	/**
	 * [UAT-1.4] Alta de jornada laboral en día no laboral
	 * DADO un día no laboral ya cargado en la agenda
	 * CUANDO agregao una jornada laboral para la fecha de dicho día no laboral
	 * ENTONCES los turnos de la nueva jornada laboral para el día no laboral, quedan deshabilitados
	 */
	@Test
	public void altaDeJornadaLaboralEnDiaNoLaboral() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralHabitual jornadaHabitual = new JornadaLaboralHabitual(VIERNES_A_LAS_09, VIERNES_A_LAS_15, _1_HORA, _2_TURNOS);
		agendaService.agregarJornadaLaboralHabitual(AGENDA_DRA_LOPEZ_ID, jornadaHabitual);

		Collection<Horario> horariosNoLaborales = diaNoLaboralService.findHorarios(VIERNES_20_DE_ENERO_ID);
		assertFalse(horariosNoLaborales.isEmpty());
		
		for (Horario horario : horariosNoLaborales)
			assertEquals(EstadoDeTurno.DESHABILITADO, horario.getEstado());
	}
	 
	/**
	 * [UAT-1.5] Baja de jornada laboral sin problemas
	 * DADA una jornada laboral ya cargada en la agenda
	 * CUANDO borro dicha jornada laboral
	 * ENTONCES veo que la jornada laboral ya no exíste para la fecha actual en adelante 
	 */
	@Test
	public void bajaDeJornadaLaboralSinProblemas() throws ExcepcionDeReglaDelNegocio {
		
		agendaService.quitarJornadaLaboralHabitual(AGENDA_DRA_LOPEZ_ID, JORNADA_HABITUAL_DRA_LOPEZ_ID, "Ya no trabajo los lunes");
		
		// Afirmo que solo queden los horarios de la jornada laboral ocasional que tenia la agenda
		Collection<Horario> horariosDeLaJornada = agendaService.findHorarios(AGENDA_DRA_LOPEZ_ID);
		assertEquals(7, horariosDeLaJornada.size());
	}
	 
	/**
	 * [UAT-1.6] Baja de jornada laboral con turnos tomados
	 * DADA una jornada laboral con turnos tomados
	 * CUANDO borro dicha jornada laboral
	 * ENTONCES
	 * 	se me solicita el motivo por el cual la estoy borrando
	 * 	veo que la jornada laboral ya no exíste para la fecha actual en adelante 
	 * 	se notifica a los clientes que sus turnos ya no son validos informandoles el motivo
	 */
	@Test
	public void bajaDeJornadaLaboralConTurnosTomados() throws ExcepcionDeReglaDelNegocio {

		Collection<Turno> turnos = clienteService.findTurnos(CLIENTE_VICTOR_ID);
		Collection<Notificacion> notificaciones = usuarioService.findNotificaciones(USUARIO_VICTOR_ID);
		
		assertEquals(1, turnos.size());
		assertTrue(notificaciones.isEmpty());
		
		agendaService.quitarJornadaLaboralHabitual(AGENDA_DRA_LOPEZ_ID, JORNADA_HABITUAL_DRA_LOPEZ_ID, "Ya no trabajo los lunes");

		Collection<Horario> horariosDeLaJornada = agendaService.findHorarios(AGENDA_DRA_LOPEZ_ID);
		assertEquals(7, horariosDeLaJornada.size());
		
		turnos = clienteService.findTurnos(CLIENTE_VICTOR_ID);
		notificaciones = usuarioService.findNotificaciones(USUARIO_VICTOR_ID);
		
		assertTrue(turnos.isEmpty());
		assertEquals(1, notificaciones.size());
	}
	 
	/**
	 * [UAT-1.7] Modificación de intervalo sin problemas
	 * DADA una jornada laboral ya cargada en la agenda
	 * CUANDO cambio su intervalo
	 * ENTONCES veo en la agenda el nuevo intervalo de la jornada laboral para la fecha actual en adelante
	 */
	@Test
	public void modificacionDeIntervaloSinProblemas() throws ExcepcionDeReglaDelNegocio {
		
		assertEquals(64, jornadaLaboralHabitualService.findHorarios(JORNADA_HABITUAL_DRA_LOPEZ_ID).size());
		
		assertTrue(usuarioService.findNotificaciones(USUARIO_VICTOR_ID).isEmpty());
		assertEquals(1, clienteService.findTurnos(CLIENTE_VICTOR_ID).size());
		
		jornadaLaboralHabitualService.modificarIntervalos(JORNADA_HABITUAL_DRA_LOPEZ_ID, LUNES_A_LAS_10, LUNES_A_LAS_20, "Se agregan 2 horarios mas los lunes");
		
		assertEquals(80, jornadaLaboralHabitualService.findHorarios(JORNADA_HABITUAL_DRA_LOPEZ_ID).size());
		
		assertTrue(usuarioService.findNotificaciones(USUARIO_VICTOR_ID).isEmpty());
		assertEquals(1, clienteService.findTurnos(CLIENTE_VICTOR_ID).size());
	}
	 
	/**
	 * [UAT-1.8] Modificación de intervalo que no es posible por duración de los turnos
	 * DADA una jornada laboral ya cargada en la agenda
	 * CUANDO intento modificar su intervalo de manera de que éste no encaje conforme a la duración de los turnos elegida
	 * ENTONCES el sistema no me deja realizar la operación
	 */
	@Test(expected=ExcepcionDeReglaDelNegocio.class)
	public void modificacionDeIntervaloQueNoEsPosiblePorDuracionDeLosTurnos() throws ExcepcionDeReglaDelNegocio {
		jornadaLaboralHabitualService.modificarIntervalos(JORNADA_HABITUAL_DRA_LOPEZ_ID, LUNES_A_LAS_10, LUNES_A_LAS_17_30, "Cambios de horario de atencion");
	}
	 
	/**
	 * [UAT-1.9] Modificación de intervalo superpuesto
	 * DADA una agenda con jornadas laborales cargadas
	 * CUANDO intento modificar el intervalo de una de ellas de modo que esta quede superpuesta con otra
	 * ENTONCES el sistema no me deja realizar la operación
	 */
	@Test(expected=ExcepcionDeReglaDelNegocio.class)
	public void modificacionDeIntervaloSuperpuesto() throws ExcepcionDeReglaDelNegocio {
		jornadaLaboralHabitualService.modificarIntervalos(JORNADA_HABITUAL_DRA_LOPEZ_ID, DOMINGO_A_LAS_12, DOMINGO_A_LAS_15, "Cambios de horario de atencion");
	}
	 
	/**
	 * [UAT-1.10] Modificación de intervalo dejando turnos fuera
	 * DADA una agenda con jornadas laborales cargadas
	 * CUANDO intento modificar el intervalo de una de ellas de modo que quito horarios en los que ya había turnos tomados
	 * ENTONCES
	 * 	se me solicita el motivo por el cual estoy borrando dichos turnos
	 * 	veo en la agenda el nuevo intervalo de la jornada laboral para la fecha actual en adelante
	 * 	se notifica a los clientes que sus turnos ya no son validos informandoles el motivo
	 */
	@Test
	public void modificacionDeIntervaloDejandoTurnosFuera() throws ExcepcionDeReglaDelNegocio {
		
		assertEquals(64, jornadaLaboralHabitualService.findHorarios(JORNADA_HABITUAL_DRA_LOPEZ_ID).size());
		
		assertTrue(usuarioService.findNotificaciones(USUARIO_VICTOR_ID).isEmpty());
		assertEquals(1, clienteService.findTurnos(CLIENTE_VICTOR_ID).size());
		
		jornadaLaboralHabitualService.modificarIntervalos(JORNADA_HABITUAL_DRA_LOPEZ_ID, LUNES_A_LAS_15, LUNES_A_LAS_20, "Cambio de horarios");
		
		assertEquals(40, jornadaLaboralHabitualService.findHorarios(JORNADA_HABITUAL_DRA_LOPEZ_ID).size());
		
		assertEquals(1, usuarioService.findNotificaciones(USUARIO_VICTOR_ID).size());
		assertTrue(clienteService.findTurnos(CLIENTE_VICTOR_ID).isEmpty());
	}
	 
	/**
	 * [UAT-1.11] Modificación de duración de turnos sin problemas
	 * DADA 
	 * 	una jornada laboral ya cargada en la agenda y
	 * 	sin turnos tomados desde la fecha actual en adelante
	 * CUANDO modifico la duración de sus turnos de manera de que esta encaje conforme a su intervalo
	 * ENTONCES veo en la agenda la jornada laboral con la nueva duración de los turnos para la fecha actual en adelante
	 */
	@Test
	public void modificacionDeDuracionDeTurnosSinProblemas() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralHabitual jornadaHabitual = sharedService.load(JornadaLaboralHabitual.class, JORNADA_HABITUAL_DRA_LOPEZ_ID);
		assertEquals(_1_HORA, jornadaHabitual.getDuracionDeLosTurnos());
		
		jornadaLaboralHabitualService.modificarDuracionDeLosTurnos(JORNADA_HABITUAL_DRA_LOPEZ_ID, _2_HORAS);
		
		jornadaHabitual = sharedService.load(JornadaLaboralHabitual.class, JORNADA_HABITUAL_DRA_LOPEZ_ID);
		assertEquals(_2_HORAS, jornadaHabitual.getDuracionDeLosTurnos());
	}
	 
	/**
	 * [UAT-1.12] Modificación de duración de turnos no posible por duración del intervalo
	 * DADA una jornada laboral ya cargada en la agenda
	 * CUANDO intento modificar la duración de sus turnos de manera de que esta no encaje conforme a su intervalo
	 * ENTONCES el sistema no me deja realizar la operación
	 */
	@Test(expected=ExcepcionDeReglaDelNegocio.class)
	public void modificacionDeDuracionDeTurnosNoPosiblePorDuracionDelIntervalo() throws ExcepcionDeReglaDelNegocio {
		jornadaLaboralHabitualService.modificarDuracionDeLosTurnos(JORNADA_HABITUAL_DRA_LOPEZ_ID, _1_30_HORAS);
	}
	 
	/**
	 * [UAT-1.13] Modificación de duración de turnos con turnos ocupados
	 * DADA
	 * 	una jornada laboral ya cargada en la agenda y
	 * 	con turnos ocupados desde la fecha actual en adelante
	 * CUANDO modifico la duración de sus turnos
	 * ENTONCES el sistema notifica a todos los clientes que la duracion de su turno ha sido modificada
	 */
	@Test
	public void modificacionDeDuracionDeTurnosConTurnosOcupados() throws ExcepcionDeReglaDelNegocio {

		Collection<Notificacion> notificaciones = usuarioService.findNotificaciones(USUARIO_VICTOR_ID);
		assertTrue(notificaciones.isEmpty());
		
		jornadaLaboralHabitualService.modificarDuracionDeLosTurnos(JORNADA_HABITUAL_DRA_LOPEZ_ID, _2_HORAS);
		
		notificaciones = usuarioService.findNotificaciones(USUARIO_VICTOR_ID);
		assertEquals(1, notificaciones.size());
	}
	 
	/**
	 * [UAT-1.14] Modificación de vacantes por turno sin problemas
	 * DADA una jornada laboral ya cargada en la agenda
	 * CUANDO modifico su cantidad de vacantes por turno
	 * ENTONCES veo en la agenda que ha cambiado la cantidad de vacantes por turno de la jornada laboral desde la fecha actual en adelante
	 * @throws ExcepcionDeReglaDelNegocio 
	 */
	@Test
	public void modificacionDeVacantesPorTurnoSinProblemas() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralHabitual jornadaHabitual = sharedService.load(JornadaLaboralHabitual.class, JORNADA_HABITUAL_DRA_LOPEZ_ID);
		
		assertEquals(3, jornadaHabitual.getVacantesPorTurno().intValue());
		assertEquals(1, clienteService.findTurnos(CLIENTE_VICTOR_ID).size());
		assertEquals(1, clienteService.findTurnos(CLIENTE_MANUEL_ID).size());
		assertTrue(usuarioService.findNotificaciones(USUARIO_VICTOR_ID).isEmpty());
		assertTrue(usuarioService.findNotificaciones(USUARIO_MANUEL_ID).isEmpty());
		
		jornadaLaboralHabitualService.quitarVacantesPorTurno(JORNADA_HABITUAL_DRA_LOPEZ_ID, 1, "La cancha que da a la calle defensa queda suspendida hasta nuevo aviso");
						
		jornadaHabitual = sharedService.load(JornadaLaboralHabitual.class, JORNADA_HABITUAL_DRA_LOPEZ_ID);
		assertEquals(2, jornadaHabitual.getVacantesPorTurno().intValue());
		assertEquals(1, clienteService.findTurnos(CLIENTE_VICTOR_ID).size());
		assertEquals(1, clienteService.findTurnos(CLIENTE_MANUEL_ID).size());
		assertTrue(usuarioService.findNotificaciones(USUARIO_VICTOR_ID).isEmpty());
		assertTrue(usuarioService.findNotificaciones(USUARIO_MANUEL_ID).isEmpty());
	}
	 
	/**
	 * [UAT-1.15] Modificación de vacantes por turno quitando vacantes ya ocupadas
	 * DADA una jornada laboral ya cargada en la agenda
	 * CUANDO quito algunas de sus vacantes por turno de forma de quitar vacantes ya ocupadas
	 * ENTONCES
	 * 	se me solicita el motivo por el cual la estoy borrando
	 * 	veo en la agenda que ha cambiado la cantidad de vacantes por turno de la jornada laboral desde la fecha actual en adelante
	 * 	se notifica a los clientes que sus turnos ya no son validos informandoles el motivo
	 */
	@Test
	public void modificacionDeVacantesPorTurnoQuitandoVacantesYaOcupadas() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralHabitual jornadaHabitual = sharedService.load(JornadaLaboralHabitual.class, JORNADA_HABITUAL_DRA_LOPEZ_ID);
		
		assertEquals(3, jornadaHabitual.getVacantesPorTurno().intValue());
		assertEquals(1, clienteService.findTurnos(CLIENTE_VICTOR_ID).size());
		assertEquals(1, clienteService.findTurnos(CLIENTE_MANUEL_ID).size());
		assertTrue(usuarioService.findNotificaciones(USUARIO_VICTOR_ID).isEmpty());
		assertTrue(usuarioService.findNotificaciones(USUARIO_MANUEL_ID).isEmpty());
		
		jornadaLaboralHabitualService.quitarVacantesPorTurno(JORNADA_HABITUAL_DRA_LOPEZ_ID, 2, "Las maquinas 3 y 4 se rompieron");
						
		jornadaHabitual = sharedService.load(JornadaLaboralHabitual.class, JORNADA_HABITUAL_DRA_LOPEZ_ID);
		assertEquals(1, jornadaHabitual.getVacantesPorTurno().intValue());
		assertEquals(1, clienteService.findTurnos(CLIENTE_VICTOR_ID).size());
		assertTrue(clienteService.findTurnos(CLIENTE_MANUEL_ID).isEmpty());
		assertTrue(usuarioService.findNotificaciones(USUARIO_VICTOR_ID).isEmpty());
		assertEquals(1, usuarioService.findNotificaciones(USUARIO_MANUEL_ID).size());
	}

}
