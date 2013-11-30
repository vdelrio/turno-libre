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

import java.util.SortedSet;

import static org.junit.Assert.*;
import static util.TestConstants.*;

public class TestJornadaLaboralOcasional {

	private static Cliente clienteJuan;
	
	/*--------------------------------- Test configuration ---------------------------------*/

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DateTimeUtils.setCurrentMillisFixed(CURRENT_TIME_MOCK);
	}
	
	@Before
	public void setUp() throws Exception {
		Usuario juan = new Usuario("Juan Lopez","juan@gmail.com", "123");
		juan.agregarRol(new Cliente());
		clienteJuan = (Cliente) juan.getRol(Cliente.NOMBRE_DE_ROL);
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		DateTimeUtils.setCurrentMillisSystem();
	}

	/*--------------------------------------------------------------------------------------*/
	/*------------------------------------- Unit tests -------------------------------------*/

	@Test(expected=IllegalArgumentException.class)
	public void debeFallarAlRecibirUnIntervaloInvalido() throws ExcepcionDeReglaDelNegocio {

		@SuppressWarnings("unused")
		JornadaLaboralOcasional jornadaLaboral = new JornadaLaboralOcasional(_25_FEB_2012_A_LAS_18, _25_FEB_2012_A_LAS_10, _1_HORA, _2_TURNOS);
	}
	
	@Test(expected=ExcepcionDeReglaDelNegocio.class)
	public void debeValidarDuracionDeLosTurnosContraLaJornadaLaboral() throws ExcepcionDeReglaDelNegocio {
		
		@SuppressWarnings("unused")
		JornadaLaboralOcasional jornadaLaboral = new JornadaLaboralOcasional(_25_FEB_2012_A_LAS_10, _25_FEB_2012_A_LAS_12, _1_30_HORAS, _2_TURNOS);
	}
	
	@Test(expected=ExcepcionDeReglaDelNegocio.class)
	public void debeFallarSiLaJornadaNoEsFutura() throws ExcepcionDeReglaDelNegocio {
		
		@SuppressWarnings("unused")
		JornadaLaboralOcasional jornadaLaboral = new JornadaLaboralOcasional(_9_ENE_2012_A_LAS_20, _9_ENE_2012_A_LAS_22, _1_HORA, _2_TURNOS);
	}
	
	@Test
	public void noDebeEntregarHorariosSiLaJornadaNoEstaLigadaAUnaAgenda() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralOcasional jornadaOcasional = new JornadaLaboralOcasional(_25_FEB_2012_A_LAS_10, _25_FEB_2012_A_LAS_18, _1_HORA, _2_TURNOS);
		assertTrue(jornadaOcasional.getHorarios().isEmpty());
	}
	
	@Test(expected=ExcepcionDeReglaDelNegocio.class)
	public void debeFallarSiLaJornadaSeSuperpone() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralOcasional jornadaOcasional = new JornadaLaboralOcasional(_25_FEB_2012_A_LAS_10, _25_FEB_2012_A_LAS_18, _1_HORA, _2_TURNOS);
		JornadaLaboralOcasional jornadaOcasionalSuperpuesta = new JornadaLaboralOcasional(_25_FEB_2012_A_LAS_15, _25_FEB_2012_A_LAS_20, _1_HORA, _2_TURNOS);
		
		Agenda agenda = new Agenda("Agenda", ANTELACION_60_DIAS);
		agenda.agregarJornadaLaboralOcasional(jornadaOcasional);
		agenda.agregarJornadaLaboralOcasional(jornadaOcasionalSuperpuesta);
	}
	
	@Test
	public void debeCrearLosHorariosYLosTurnos() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralOcasional jornadaOcasional = new JornadaLaboralOcasional(_25_FEB_2012_A_LAS_10, _25_FEB_2012_A_LAS_18, _1_HORA, _2_TURNOS);
		
		Agenda agenda = new Agenda("Agenda", ANTELACION_60_DIAS);
		agenda.agregarJornadaLaboralOcasional(jornadaOcasional);
		
		assertTrue(agenda.getJornadasLaboralesOcasionales().contains(jornadaOcasional));
		assertEquals(8, agenda.getHorarios().size());
		assertEquals(8, jornadaOcasional.getHorarios().size());
		
		DateTime fechaYHoraDelHorarioEsperados = _25_FEB_2012_A_LAS_10;
		
		for (Horario horario : agenda.getHorarios()) {
			
			assertEquals(_1_HORA, horario.getIntervalo().toDuration());
			assertEquals(_2_TURNOS, horario.getTurnos().size());
			assertEquals(fechaYHoraDelHorarioEsperados, horario.getIntervalo().getStart());
			assertEquals(fechaYHoraDelHorarioEsperados.plus(_1_HORA), horario.getIntervalo().getEnd());
			
			fechaYHoraDelHorarioEsperados = fechaYHoraDelHorarioEsperados.plus(_1_HORA);
		}
	}
	
	@Test
	public void debeDevolverLosHorarios() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralOcasional jornadaLaboralDeRelleno = new JornadaLaboralOcasional(_25_FEB_2012_A_LAS_10, _25_FEB_2012_A_LAS_12, _1_HORA, _2_TURNOS);
		JornadaLaboralOcasional jornadaLaboralAUtilizar = new JornadaLaboralOcasional(_25_FEB_2012_A_LAS_15, _25_FEB_2012_A_LAS_20, _1_HORA, _2_TURNOS);
		
		Agenda agenda = new Agenda("Agenda", ANTELACION_60_DIAS);
		agenda.agregarJornadaLaboralOcasional(jornadaLaboralDeRelleno);
		agenda.agregarJornadaLaboralOcasional(jornadaLaboralAUtilizar);
		
		SortedSet<Horario> horariosObtenidos = jornadaLaboralAUtilizar.getHorarios();
		assertEquals(5, horariosObtenidos.size());
	}
	
	@Test
	public void debeBorrarLosHorariosYLosTurnosSinTurnosTomados() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralOcasional jornadaOcasional = new JornadaLaboralOcasional(_25_FEB_2012_A_LAS_10, _25_FEB_2012_A_LAS_18, _1_HORA, _2_TURNOS);
		
		Agenda agenda = new Agenda("Agenda", ANTELACION_60_DIAS);
		agenda.agregarJornadaLaboralOcasional(jornadaOcasional);
		
		assertTrue(agenda.getJornadasLaboralesOcasionales().contains(jornadaOcasional));
		assertEquals(8, agenda.getHorarios().size());
		assertEquals(8, jornadaOcasional.getHorarios().size());
		
		agenda.quitarJornadaLaboralOcasional(jornadaOcasional, "Ya no trabajamos en el dia de la madre");
		
		assertFalse(agenda.getJornadasLaboralesOcasionales().contains(jornadaOcasional));
		assertTrue(agenda.getHorarios().isEmpty());
		assertNull(jornadaOcasional.getAgenda());
	}
	
	@Test
	public void debeBorrarLosHorariosYLosTurnosConTurnosTomados() throws ExcepcionDeReglaDelNegocio {
		
		PrestadorDeServicios prestador = new PrestadorDeServicios("Prestador");
		Agenda agenda = new Agenda("Agenda", ANTELACION_60_DIAS);
		prestador.agregarAgenda(agenda);
		
		JornadaLaboralOcasional jornadaOcasional = new JornadaLaboralOcasional(_25_FEB_2012_A_LAS_10, _25_FEB_2012_A_LAS_18, _1_HORA, _2_TURNOS);
		agenda.agregarJornadaLaboralOcasional(jornadaOcasional);
		
		assertTrue(agenda.getJornadasLaboralesOcasionales().contains(jornadaOcasional));
		assertEquals(8, agenda.getHorarios().size());
		assertEquals(8, jornadaOcasional.getHorarios().size());
		
		Horario horario = agenda.getHorario(new Interval(_25_FEB_2012_A_LAS_17, _25_FEB_2012_A_LAS_18));
		assertEquals(2, horario.getTurnos(EstadoDeTurno.LIBRE).size());
		
		horario.sacarTurno(clienteJuan);
		
		assertTrue(clienteJuan.getUsuario().getNotificaciones().isEmpty());
		assertEquals(1, clienteJuan.getTurnos().size());
		assertEquals(1, horario.getTurnos(EstadoDeTurno.LIBRE).size());
		
		agenda.quitarJornadaLaboralOcasional(jornadaOcasional, "Ya no trabajamos en el dia de la madre");
		
		assertFalse(agenda.getJornadasLaboralesOcasionales().contains(jornadaOcasional));
		assertTrue(agenda.getHorarios().isEmpty());
		assertNull(jornadaOcasional.getAgenda());
		
		assertEquals(1, clienteJuan.getUsuario().getNotificaciones().size());
		assertTrue(clienteJuan.getTurnos().isEmpty());
		assertEquals(_2_TURNOS, horario.getTurnos(EstadoDeTurno.LIBRE).size());
	}
	
	@Test
	public void debeModificarIntervalosQuitandoTurnos() throws ExcepcionDeReglaDelNegocio {
		
		PrestadorDeServicios prestador = new PrestadorDeServicios("Prestador");
		Agenda agenda = new Agenda("Agenda", ANTELACION_60_DIAS);
		prestador.agregarAgenda(agenda);
		
		JornadaLaboralOcasional jornadaOcasional = new JornadaLaboralOcasional(_25_FEB_2012_A_LAS_10, _25_FEB_2012_A_LAS_18, _1_HORA, _2_TURNOS);
		agenda.agregarJornadaLaboralOcasional(jornadaOcasional);
		
		assertTrue(agenda.getJornadasLaboralesOcasionales().contains(jornadaOcasional));
		assertEquals(8, agenda.getHorarios().size());
		assertEquals(8, jornadaOcasional.getHorarios().size());
		
		Horario horario = agenda.getHorario(new Interval(_25_FEB_2012_A_LAS_17, _25_FEB_2012_A_LAS_18));
		horario.sacarTurno(clienteJuan);
		
		assertTrue(clienteJuan.getUsuario().getNotificaciones().isEmpty());
		assertEquals(1, clienteJuan.getTurnos().size());
		assertEquals(1, horario.getTurnos(EstadoDeTurno.LIBRE).size());
		
		jornadaOcasional.modificarIntervalo(new Interval(_25_FEB_2012_A_LAS_10, _25_FEB_2012_A_LAS_15), "Reestructuracion de horarios");
		
		assertTrue(agenda.getJornadasLaboralesOcasionales().contains(jornadaOcasional));
		assertEquals(5, agenda.getHorarios().size());
		assertEquals(5, jornadaOcasional.getHorarios().size());
		
		assertEquals(1, clienteJuan.getUsuario().getNotificaciones().size());
		assertTrue(clienteJuan.getTurnos().isEmpty());
		assertEquals(_2_TURNOS, horario.getTurnos(EstadoDeTurno.LIBRE).size());
	}	
	
	@Test
	public void debeModificarIntervalosAgregandoTurnos() throws ExcepcionDeReglaDelNegocio {
		
		PrestadorDeServicios prestador = new PrestadorDeServicios("Prestador");
		Agenda agenda = new Agenda("Agenda", ANTELACION_60_DIAS);
		prestador.agregarAgenda(agenda);
		
		JornadaLaboralOcasional jornadaOcasional = new JornadaLaboralOcasional(_25_FEB_2012_A_LAS_10, _25_FEB_2012_A_LAS_15, _1_HORA, _2_TURNOS);
		agenda.agregarJornadaLaboralOcasional(jornadaOcasional);
		
		assertTrue(agenda.getJornadasLaboralesOcasionales().contains(jornadaOcasional));
		assertEquals(5, agenda.getHorarios().size());
		assertEquals(5, jornadaOcasional.getHorarios().size());
		
		Horario horario = agenda.getHorario(new Interval(_25_FEB_2012_A_LAS_13, _25_FEB_2012_A_LAS_14));
		horario.sacarTurno(clienteJuan);
		
		assertTrue(clienteJuan.getUsuario().getNotificaciones().isEmpty());
		assertEquals(1, clienteJuan.getTurnos().size());
		assertEquals(1, horario.getTurnos(EstadoDeTurno.LIBRE).size());
		
		jornadaOcasional.modificarIntervalo(new Interval(_25_FEB_2012_A_LAS_10, _25_FEB_2012_A_LAS_18), "Reestructuracion de horarios");
		
		assertTrue(agenda.getJornadasLaboralesOcasionales().contains(jornadaOcasional));
		assertEquals(8, agenda.getHorarios().size());
		assertEquals(8, jornadaOcasional.getHorarios().size());
		
		assertTrue(clienteJuan.getUsuario().getNotificaciones().isEmpty());
		assertEquals(1, clienteJuan.getTurnos().size());
		assertEquals(1, horario.getTurnos(EstadoDeTurno.LIBRE).size());
	}
	
	/*--------------------------------------------------------------------------------------*/
	
}
