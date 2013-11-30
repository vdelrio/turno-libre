package com.turnolibre.business.turno;

import com.turnolibre.business.agenda.Agenda;
import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.prestador.PrestadorDeServicios;
import com.turnolibre.business.usuario.Cliente;
import com.turnolibre.business.usuario.Usuario;
import com.turnolibre.util.CollectionUtils;
import org.joda.time.DateTimeUtils;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static util.TestConstants.*;

public class TestHorario {

	private static final PrestadorDeServicios PRESTADOR = new PrestadorDeServicios("Tito papi");
	private static final Agenda AGENDA = new Agenda("", Period.years(10));
	
	private static Cliente clienteJuancito;
	private static Cliente clienteVictor;
	

    /*--------------------------------- Test configuration ---------------------------------*/

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        DateTimeUtils.setCurrentMillisFixed(CURRENT_TIME_MOCK);
        PRESTADOR.agregarAgenda(AGENDA);
    }

    @Before
    public void setUp() throws Exception {

        Usuario juancito = new Usuario("Juancito Lopez", "juancito@gmail.com", "123456");
        Usuario victor = new Usuario("Victor Del Rio","victor@gmail.com", "123456");
        juancito.agregarRol(new Cliente());
        victor.agregarRol(new Cliente());
        clienteJuancito = (Cliente) juancito.getRol(Cliente.NOMBRE_DE_ROL);
        clienteVictor = (Cliente) victor.getRol(Cliente.NOMBRE_DE_ROL);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
    }

	/*--------------------------------------------------------------------------------------*/
    /*------------------------------------- Unit tests -------------------------------------*/
	
	@Test(expected=ExcepcionDeReglaDelNegocio.class)
	public void laDuracionDelIntervaloDebeSerMayorOIgualALaDuracionMinima() throws ExcepcionDeReglaDelNegocio {
		
		@SuppressWarnings("unused")
		Horario horario = new Horario(new Interval(_9_ENE_2012_A_LAS_10, _9_ENE_2012_A_LAS_10_9), 1, AGENDA);
	}
	
	@Test(expected=ExcepcionDeReglaDelNegocio.class)
	public void elHorarioDebeContenerAlMenosUnTurno() throws ExcepcionDeReglaDelNegocio {
		
		@SuppressWarnings("unused")
		Horario horario = new Horario(new Interval(_9_ENE_2012_A_LAS_10, _9_ENE_2012_A_LAS_18), 0, AGENDA);
	}
	
	@Test
	public void losTurnosSeDebenCrearComoLibres() throws ExcepcionDeReglaDelNegocio {
		
		Horario horario = new Horario(new Interval(_9_ENE_2012_A_LAS_10, _9_ENE_2012_A_LAS_18), 3, AGENDA);
		
		assertEquals(3, horario.getTurnos().size());
		
		for (Turno turno : horario.getTurnos()) {
			assertTrue(turno.estaLibre());
		}
	}
	
	@Test
	public void siTodosLosTurnosEstanOcupadosNoSePuedeSacarTurno() throws ExcepcionDeReglaDelNegocio {
		
		Horario horario = new Horario(new Interval(_9_ENE_2012_A_LAS_10, _9_ENE_2012_A_LAS_18), 1, AGENDA);
		
		for (Turno turno : horario.getTurnos()) 
			turno.setEstado(EstadoDeTurno.OCUPADO);
		
		assertNull(horario.sacarTurno(clienteJuancito));
	}
	
	@Test(expected=ExcepcionDeReglaDelNegocio.class)
	public void siYaTengoUnTurnoEnUnHorarioNoPuedoSacarOtroEnElMismo() throws ExcepcionDeReglaDelNegocio {
		
		Horario horario = new Horario(new Interval(_9_ENE_2012_A_LAS_10, _9_ENE_2012_A_LAS_18), 2, AGENDA);
		horario.sacarTurno(clienteJuancito);
		horario.sacarTurno(clienteJuancito);
	}
	
	@Test
	public void siHayAlgunTurnoLibreSeDebePoderSacarTurno() throws ExcepcionDeReglaDelNegocio {
		
		Horario horario = new Horario(new Interval(_9_ENE_2012_A_LAS_10, _9_ENE_2012_A_LAS_18), 2, AGENDA);
		CollectionUtils.asList(horario.getTurnos()).get(0).setEstado(EstadoDeTurno.OCUPADO);
		
		assertEquals(EstadoDeTurno.LIBRE, horario.getEstado());
		assertTrue(clienteJuancito.getTurnos().isEmpty());

		Turno turno = horario.sacarTurno(clienteJuancito);
		
		assertEquals(EstadoDeTurno.OCUPADO, horario.getEstado());
		assertEquals(1, clienteJuancito.getTurnos().size());
		
		assertNotNull(turno);
		assertEquals(clienteJuancito, turno.getCliente());
		assertEquals(EstadoDeTurno.OCUPADO, turno.getEstado());
	}
	
	@Test
	public void debeIncrementarLaCantidadDeLosTurnos() throws ExcepcionDeReglaDelNegocio {
		
		Horario horario = new Horario(new Interval(_9_ENE_2012_A_LAS_10, _9_ENE_2012_A_LAS_18), 3, AGENDA);
		
		horario.sacarTurno(clienteJuancito);
		
		assertEquals(3, horario.getTurnos().size());
		assertEquals(1, horario.getTurnos(EstadoDeTurno.OCUPADO).size());
		assertEquals(2, horario.getTurnos(EstadoDeTurno.LIBRE).size());
		
		horario.agregarTurnos(2);
		
		assertEquals(5, horario.getTurnos().size());
		assertEquals(1, horario.getTurnos(EstadoDeTurno.OCUPADO).size());
		assertEquals(4, horario.getTurnos(EstadoDeTurno.LIBRE).size());
	}
	
	@Test
	public void debeDecrementarLaCantidadDeLosTurnosPrimeroDeshabilitados() throws ExcepcionDeReglaDelNegocio {
		
		Horario horario = new Horario(new Interval(_9_ENE_2012_A_LAS_10, _9_ENE_2012_A_LAS_18), 3, AGENDA);
		horario.getTurno(1).setEstado(EstadoDeTurno.DESHABILITADO);
				
		horario.sacarTurno(clienteJuancito);
		
		assertEquals(3, horario.getTurnos().size());
		assertEquals(EstadoDeTurno.DESHABILITADO, horario.getTurno(1).getEstado());
		assertEquals(EstadoDeTurno.OCUPADO, horario.getTurno(2).getEstado());
		assertEquals(EstadoDeTurno.LIBRE, horario.getTurno(3).getEstado());
		
		horario.quitarTurnos(1, "La cancha que da a la calle defensa queda suspendida hasta nuevo aviso");
		
		assertEquals(2, horario.getTurnos().size());
		assertEquals(EstadoDeTurno.OCUPADO, horario.getTurno(1).getEstado());
		assertEquals(EstadoDeTurno.LIBRE, horario.getTurno(2).getEstado());
		assertNull(horario.getTurno(3));
	}
	
	@Test
	public void debeDecrementarLaCantidadDeLosTurnosSinNotificaciones() throws ExcepcionDeReglaDelNegocio {
		
		Horario horario = new Horario(new Interval(_9_ENE_2012_A_LAS_10, _9_ENE_2012_A_LAS_18), 3, AGENDA);
		horario.getTurno(1).setEstado(EstadoDeTurno.DESHABILITADO);
				
		horario.sacarTurno(clienteJuancito);
		
		assertEquals(3, horario.getTurnos().size());
		assertEquals(EstadoDeTurno.DESHABILITADO, horario.getTurno(1).getEstado());
		assertEquals(EstadoDeTurno.OCUPADO, horario.getTurno(2).getEstado());
		assertEquals(EstadoDeTurno.LIBRE, horario.getTurno(3).getEstado());
		
		horario.quitarTurnos(2, "Se rompieron 2 maquinas de quinesionlogia");
		
		assertEquals(1, horario.getTurnos().size());
		assertEquals(EstadoDeTurno.OCUPADO, horario.getTurno(1).getEstado());
		assertNull(horario.getTurno(2));
		assertNull(horario.getTurno(3));
	}
	
	@Test
	public void debeDecrementarLaCantidadDeLosTurnosConNotificaciones() throws ExcepcionDeReglaDelNegocio {
		
		Horario horario = new Horario(new Interval(_9_ENE_2012_A_LAS_10, _9_ENE_2012_A_LAS_18), 3, AGENDA);
		horario.sacarTurno(clienteJuancito);
		horario.sacarTurno(clienteVictor);
		
		assertEquals(3, horario.getTurnos().size());
		assertEquals(EstadoDeTurno.OCUPADO, horario.getTurno(1).getEstado());
		assertEquals(EstadoDeTurno.OCUPADO, horario.getTurno(2).getEstado());
		assertEquals(EstadoDeTurno.LIBRE, horario.getTurno(3).getEstado());
		assertTrue(clienteJuancito.getUsuario().getNotificaciones().isEmpty());
		assertTrue(clienteVictor.getUsuario().getNotificaciones().isEmpty());
		
		horario.quitarTurnos(2, "Se rompieron 2 maquinas de quinesionlogia");
		
		assertEquals(1, horario.getTurnos().size());
		assertEquals(EstadoDeTurno.OCUPADO, horario.getTurno(1).getEstado());
		assertEquals(clienteJuancito, horario.getTurno(1).getCliente());
		assertNull(horario.getTurno(2));
		assertNull(horario.getTurno(3));
		assertTrue(clienteJuancito.getUsuario().getNotificaciones().isEmpty());
		assertEquals(1, clienteVictor.getUsuario().getNotificaciones().size());
	}

    /*--------------------------------------------------------------------------------------*/
	
}
