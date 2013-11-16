package com.turnolibre.business.agenda;

import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.prestador.PrestadorDeServicios;
import com.turnolibre.business.turno.EstadoDeTurno;
import com.turnolibre.business.turno.Horario;
import com.turnolibre.business.usuario.Cliente;
import com.turnolibre.business.usuario.Usuario;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.Interval;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Iterator;
import java.util.SortedSet;

import static org.junit.Assert.*;
import static util.TestConstants.*;

public class TestJornadaLaboralHabitual {

	private static Cliente clienteJuan;
	
	
	/*--------------------------------- Test configuration ---------------------------------*/

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DateTimeUtils.setCurrentMillisFixed(new DateTime(2012, 1, 11, 21, 47).getMillis());
		
	}
	
	@Before
	public void setUp() throws Exception {
		Usuario juan = new Usuario("Juan Lopez", "juan@gmail.com", "123");
		juan.agregarRol(new Cliente());
		clienteJuan = (Cliente) juan.getRol(Cliente.NOMBRE_DE_ROL);
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		DateTimeUtils.setCurrentMillisSystem();
	}

	/*--------------------------------------------------------------------------------------*/
	/*------------------------------------- Unit tests -------------------------------------*/
	
	@Test(expected=ExcepcionDeReglaDelNegocio.class)
	public void debeValidarDuracionDeLosTurnosContraLaJornadaLaboral() throws ExcepcionDeReglaDelNegocio {
		
		@SuppressWarnings("unused")
		JornadaLaboralHabitual jornadaLaboral = new JornadaLaboralHabitual(LUNES_A_LAS_10, LUNES_A_LAS_12, _1_30_HORAS, _2_TURNOS);
	}
	
	@Test
	public void noDebeEntregarHorariosSiLaJornadaNoEstaLigadaAUnaAgenda() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralHabitual jornadaHabitual = new JornadaLaboralHabitual(LUNES_A_LAS_10, LUNES_A_LAS_18, _1_HORA, _2_TURNOS);
		assertTrue(jornadaHabitual.getHorarios().isEmpty());
	}
	
	@Test(expected=ExcepcionDeReglaDelNegocio.class)
	public void debeFallarSiLaJornadaSeSuperponeConOtraHabitual() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralHabitual jornadaHabitual = new JornadaLaboralHabitual(LUNES_A_LAS_10, LUNES_A_LAS_18, _1_HORA, _2_TURNOS);
		JornadaLaboralHabitual jornadaHabitualSuperpuesta = new JornadaLaboralHabitual(LUNES_A_LAS_15, LUNES_A_LAS_20, _1_HORA, _2_TURNOS);
		
		Agenda agenda = new Agenda("Agenda", ANTELACION_1_MES);
		agenda.agregarJornadaLaboralHabitual(jornadaHabitual);
		agenda.agregarJornadaLaboralHabitual(jornadaHabitualSuperpuesta);
	}
	
	@Test(expected=ExcepcionDeReglaDelNegocio.class)
	public void debeFallarSiLaJornadaSeSuperponeConOtraOcasional() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralOcasional jornadaOcasional = new JornadaLaboralOcasional(LUN_10_DIC_2012_A_LAS_10, LUN_10_DIC_2012_A_LAS_18, _1_HORA, _2_TURNOS);
		JornadaLaboralHabitual jornadaHabitualSuperpuesta = new JornadaLaboralHabitual(LUNES_A_LAS_15, LUNES_A_LAS_20, _1_HORA, _2_TURNOS);
		
		Agenda agenda = new Agenda("Agenda", ANTELACION_1_MES);
		agenda.agregarJornadaLaboralOcasional(jornadaOcasional);
		agenda.agregarJornadaLaboralHabitual(jornadaHabitualSuperpuesta);
	}
	
	@Test
	public void debeCrearLosHorariosYLosTurnos() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralHabitual jornadaHabitual = new JornadaLaboralHabitual(LUNES_A_LAS_10, LUNES_A_LAS_18, _1_HORA, _2_TURNOS);
		
		Agenda agenda = new Agenda("Agenda", ANTELACION_1_MES);
		agenda.agregarJornadaLaboralHabitual(jornadaHabitual);
		
		assertEquals(32, agenda.getHorarios().size());
		
		Iterator<Horario> horarios = agenda.getHorarios().iterator();
		DateTime fechaYHora = _16_ENE_2012_A_LAS_10;
				
		do {
			
			int cantidadDeTurnos = 0;
			while ( horarios.hasNext() && ++cantidadDeTurnos <= 8) {
				
				Horario horario = horarios.next();
				
				assertEquals(_1_HORA, horario.getIntervalo().toDuration());
				assertEquals(_2_TURNOS, horario.getTurnos().size());
				assertEquals(fechaYHora, horario.getIntervalo().getStart());
				assertEquals(fechaYHora.plus(_1_HORA), horario.getIntervalo().getEnd());
				
				fechaYHora = fechaYHora.plus(_1_HORA);
			}
			
			fechaYHora = fechaYHora.plusWeeks(1).withHourOfDay(10);
			
		} while ( fechaYHora.isBefore(DateTime.now().plus(ANTELACION_1_MES)) );
	}
	
	@Test
	public void debeDeshabilitarLosTurnosEnDiaNoLaboral() throws ExcepcionDeReglaDelNegocio {
		
		DiaNoLaboral diaNoLaboral = new DiaNoLaboral(_20_DE_ENERO_DEL_2012, "Dia del invento");
		JornadaLaboralHabitual jornadaHabitual = new JornadaLaboralHabitual(VIERNES_A_LAS_09, VIERNES_A_LAS_15, _1_HORA, _2_TURNOS);
		
		Agenda agenda = new Agenda("Agenda", ANTELACION_1_MES);
		agenda.agregarDiaNoLaboral(diaNoLaboral);
		agenda.agregarJornadaLaboralHabitual(jornadaHabitual);
		
		SortedSet<Horario> horariosNoLaborales = diaNoLaboral.getHorarios();
		assertFalse(horariosNoLaborales.isEmpty());
		
		for (Horario horario : horariosNoLaborales) {
			
			assertEquals(_1_HORA, horario.getIntervalo().toDuration());
			assertEquals(_2_TURNOS, horario.getTurnos().size());
			assertEquals(EstadoDeTurno.DESHABILITADO, horario.getEstado());
		}
	}
	
	@Test
	public void debeDevolverLosHorarios() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralHabitual jornadaLaboralDeRelleno = new JornadaLaboralHabitual(LUNES_A_LAS_10, LUNES_A_LAS_12, _1_HORA, _2_TURNOS);
		JornadaLaboralHabitual jornadaLaboralAUtilizar = new JornadaLaboralHabitual(LUNES_A_LAS_15, LUNES_A_LAS_20, _1_HORA, _2_TURNOS);
		
		Agenda agenda = new Agenda("Agenda", ANTELACION_1_MES);
		agenda.agregarJornadaLaboralHabitual(jornadaLaboralDeRelleno);
		agenda.agregarJornadaLaboralHabitual(jornadaLaboralAUtilizar);
		
		assertEquals(20, jornadaLaboralAUtilizar.getHorarios().size());
	}
	
	@Test
	public void debeCompletarLosHorariosYLosTurnosFaltantes() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralHabitual jornadaHabitual = new JornadaLaboralHabitual(LUNES_A_LAS_10, LUNES_A_LAS_18, _1_HORA, _2_TURNOS);
		
		Agenda agenda = new Agenda("Agenda", ANTELACION_1_MES);
		agenda.agregarJornadaLaboralHabitual(jornadaHabitual);
		
		assertEquals(32, jornadaHabitual.getHorarios().size());
		
		// Nos movemos una semana hacia adelante
		DateTimeUtils.setCurrentMillisFixed(new DateTime(2012, 1, 18, 21, 47).getMillis());
	
		assertEquals(24, jornadaHabitual.getHorarios().size());
		jornadaHabitual.actualizarHorarios();
		assertEquals(32, jornadaHabitual.getHorarios().size());
		
		DateTimeUtils.setCurrentMillisFixed(CURRENT_TIME_MOCK);
	}
	
	@Test
	public void debeBorrarLosHorariosYLosTurnosSinTurnosTomados() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralHabitual jornadaHabitual = new JornadaLaboralHabitual(LUNES_A_LAS_10, LUNES_A_LAS_18, _1_HORA, _2_TURNOS);
		
		Agenda agenda = new Agenda("Agenda", ANTELACION_1_MES);
		agenda.agregarJornadaLaboralHabitual(jornadaHabitual);
		
		assertTrue(agenda.getJornadasLaboralesHabituales().contains(jornadaHabitual));
		assertEquals(32, agenda.getHorarios().size());
		assertEquals(32, jornadaHabitual.getHorarios().size());
		
		agenda.quitarJornadaLaboralHabitual(jornadaHabitual, "Ya no trabajamos los lunes");
		
		assertFalse(agenda.getJornadasLaboralesHabituales().contains(jornadaHabitual));
		assertTrue(agenda.getHorarios().isEmpty());
		assertNull(jornadaHabitual.getAgenda());
	}
	
	@Test
	public void debeBorrarLosHorariosYLosTurnosConTurnosTomados() throws ExcepcionDeReglaDelNegocio {
		
		PrestadorDeServicios prestador = new PrestadorDeServicios("Prestador", "Av. Siempreviva", "45875544");
		Agenda agenda = new Agenda("Agenda", ANTELACION_1_MES);
		prestador.agregarAgenda(agenda);
		
		JornadaLaboralHabitual jornadaHabitual = new JornadaLaboralHabitual(LUNES_A_LAS_10, LUNES_A_LAS_18, _1_HORA, _2_TURNOS);
		agenda.agregarJornadaLaboralHabitual(jornadaHabitual);
		
		assertTrue(agenda.getJornadasLaboralesHabituales().contains(jornadaHabitual));
		assertEquals(32, agenda.getHorarios().size());
		assertEquals(32, jornadaHabitual.getHorarios().size());
		
		Horario horario = agenda.getHorario(new Interval(_16_ENE_2012_A_LAS_13, _16_ENE_2012_A_LAS_14));
		horario.sacarTurno(clienteJuan);
		
		assertTrue(clienteJuan.getUsuario().getNotificaciones().isEmpty());
		assertEquals(1, clienteJuan.getTurnos().size());
		assertEquals(1, horario.getTurnos(EstadoDeTurno.LIBRE).size());
		
		agenda.quitarJornadaLaboralHabitual(jornadaHabitual, "Ya no trabajamos los lunes");
		
		assertFalse(agenda.getJornadasLaboralesHabituales().contains(jornadaHabitual));
		assertTrue(agenda.getHorarios().isEmpty());
		assertNull(jornadaHabitual.getAgenda());
		
		assertEquals(1, clienteJuan.getUsuario().getNotificaciones().size());
		assertTrue(clienteJuan.getTurnos().isEmpty());
		assertEquals(_2_TURNOS, horario.getTurnos(EstadoDeTurno.LIBRE).size());
	}
	
	@Test
	public void debeModificarIntervalosQuitandoTurnos() throws ExcepcionDeReglaDelNegocio {
		
		PrestadorDeServicios prestador = new PrestadorDeServicios("Prestador", "Av. Siempreviva", "45875544");
		Agenda agenda = new Agenda("Agenda", ANTELACION_1_MES);
		prestador.agregarAgenda(agenda);
		
		JornadaLaboralHabitual jornadaHabitual = new JornadaLaboralHabitual(LUNES_A_LAS_10, LUNES_A_LAS_18, _1_HORA, _2_TURNOS);
		agenda.agregarJornadaLaboralHabitual(jornadaHabitual);
		
		assertTrue(agenda.getJornadasLaboralesHabituales().contains(jornadaHabitual));
		assertEquals(32, agenda.getHorarios().size());
		assertEquals(32, jornadaHabitual.getHorarios().size());
		
		Horario horario = agenda.getHorario(new Interval(_16_ENE_2012_A_LAS_16, _16_ENE_2012_A_LAS_17));
		horario.sacarTurno(clienteJuan);
		
		assertTrue(clienteJuan.getUsuario().getNotificaciones().isEmpty());
		assertEquals(1, clienteJuan.getTurnos().size());
		assertEquals(1, horario.getTurnos(EstadoDeTurno.LIBRE).size());
		
		jornadaHabitual.modificarIntervalos(LUNES_A_LAS_10, LUNES_A_LAS_15, "Reestructuracion de horarios");
		
		assertTrue(agenda.getJornadasLaboralesHabituales().contains(jornadaHabitual));
		assertEquals(20, agenda.getHorarios().size());
		assertEquals(20, jornadaHabitual.getHorarios().size());
		
		assertEquals(1, clienteJuan.getUsuario().getNotificaciones().size());
		assertTrue(clienteJuan.getTurnos().isEmpty());
		assertEquals(_2_TURNOS, horario.getTurnos(EstadoDeTurno.LIBRE).size());
	}	
	
	@Test
	public void debeModificarIntervalosAgregandoTurnos() throws ExcepcionDeReglaDelNegocio {
		
		PrestadorDeServicios prestador = new PrestadorDeServicios("Prestador", "Av. Siempreviva", "45875544");
		Agenda agenda = new Agenda("Agenda", ANTELACION_1_MES);
		prestador.agregarAgenda(agenda);
		
		JornadaLaboralHabitual jornadaHabitual = new JornadaLaboralHabitual(LUNES_A_LAS_10, LUNES_A_LAS_15, _1_HORA, _2_TURNOS);
		agenda.agregarJornadaLaboralHabitual(jornadaHabitual);
		
		assertTrue(agenda.getJornadasLaboralesHabituales().contains(jornadaHabitual));
		assertEquals(20, agenda.getHorarios().size());
		assertEquals(20, jornadaHabitual.getHorarios().size());
		
		Horario horario = agenda.getHorario(new Interval(_16_ENE_2012_A_LAS_13, _16_ENE_2012_A_LAS_14));
		horario.sacarTurno(clienteJuan);
		
		assertTrue(clienteJuan.getUsuario().getNotificaciones().isEmpty());
		assertEquals(1, clienteJuan.getTurnos().size());
		assertEquals(1, horario.getTurnos(EstadoDeTurno.LIBRE).size());
		
		jornadaHabitual.modificarIntervalos(LUNES_A_LAS_10, LUNES_A_LAS_18, "Reestructuracion de horarios");
		
		assertTrue(agenda.getJornadasLaboralesHabituales().contains(jornadaHabitual));
		assertEquals(32, agenda.getHorarios().size());
		assertEquals(32, jornadaHabitual.getHorarios().size());
		
		assertTrue(clienteJuan.getUsuario().getNotificaciones().isEmpty());
		assertEquals(1, clienteJuan.getTurnos().size());
		assertEquals(1, horario.getTurnos(EstadoDeTurno.LIBRE).size());
	}
	
	/*--------------------------------------------------------------------------------------*/
	
}
