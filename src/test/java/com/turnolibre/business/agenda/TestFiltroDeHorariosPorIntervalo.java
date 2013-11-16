package com.turnolibre.business.agenda;

import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.turno.Horario;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.SortedSet;

import static org.junit.Assert.assertEquals;
import static util.TestConstants.*;

public class TestFiltroDeHorariosPorIntervalo {
	

	/*--------------------------------- Test configuration ---------------------------------*/

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DateTimeUtils.setCurrentMillisFixed(new DateTime(2012, 1, 12, 9, 24).getMillis());
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		DateTimeUtils.setCurrentMillisSystem();
	}

	/*--------------------------------------------------------------------------------------*/
	/*------------------------------------- Unit tests -------------------------------------*/

	@Test // Fuera del intervalo
	public void debeDevolverLosHorariosDelIntervaloFuera() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralOcasional jornadaLaboral = new JornadaLaboralOcasional(_22_DIC_2012_A_LAS_10, _22_DIC_2012_A_LAS_18, _1_HORA, _2_TURNOS);
		
		Agenda agenda = new Agenda("Agenda", ANTELACION_2_MESES);
		agenda.agregarJornadaLaboralOcasional(jornadaLaboral);
		
		SortedSet<Horario> horariosFiltrados = FiltroDeHorarios.ejecutar(agenda.getHorarios(), _12_ENE_2012_A_LAS_6, _22_DIC_2012_A_LAS_15);
		assertEquals(5, horariosFiltrados.size());
	}
	
	@Test // Intervalo completo
	public void debeDevolverLosHorariosDelIntervaloCompleto() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralOcasional jornadaOcasional = new JornadaLaboralOcasional(_22_DIC_2012_A_LAS_10, _22_DIC_2012_A_LAS_18, _1_HORA, _2_TURNOS);
		
		Agenda agenda = new Agenda("Agenda", ANTELACION_2_MESES);
		agenda.agregarJornadaLaboralOcasional(jornadaOcasional);
		
		SortedSet<Horario> horariosFiltrados = FiltroDeHorarios.ejecutar(jornadaOcasional.getHorarios(), _22_DIC_2012_A_LAS_10, _22_DIC_2012_A_LAS_18);
		
		assertEquals(8, horariosFiltrados.size());
		assertEquals(agenda.getHorarios(), horariosFiltrados);
	}
	
	@Test // Parte del intervalo
	public void debeDevolverLosHorariosDelIntervaloParte() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralOcasional jornadaOcasional = new JornadaLaboralOcasional(_22_DIC_2012_A_LAS_10, _22_DIC_2012_A_LAS_18, _1_HORA, _2_TURNOS);
		
		Agenda agenda = new Agenda("Agenda", ANTELACION_2_MESES);
		agenda.agregarJornadaLaboralOcasional(jornadaOcasional);
		
		SortedSet<Horario> horariosFiltrados = FiltroDeHorarios.ejecutar(jornadaOcasional.getHorarios(), _22_DIC_2012_A_LAS_11, _22_DIC_2012_A_LAS_17);
		assertEquals(6, horariosFiltrados.size());
	}
	
	@Test // Parte del intervalo con jornada habitual
	public void debeDevolverLosHorariosDelIntervaloJornadaHabitual() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralHabitual jornadaHabitual = new JornadaLaboralHabitual(LUNES_A_LAS_10, LUNES_A_LAS_18, _30_MINUTOS, _2_TURNOS);
		
		Agenda agenda = new Agenda("Agenda", ANTELACION_2_MESES);
		agenda.agregarJornadaLaboralHabitual(jornadaHabitual);
		
		SortedSet<Horario> horariosObtenidos = FiltroDeHorarios.ejecutar(jornadaHabitual.getHorarios(), _16_ENE_2012_A_LAS_11, _16_ENE_2012_A_LAS_17);
		assertEquals(12, horariosObtenidos.size());
	}
	
	@Test // Ultimo horario cortado
	public void debeDevolverLosHorariosDelIntervaloCortado() throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralHabitual jornadaHabitual = new JornadaLaboralHabitual(LUNES_A_LAS_10, LUNES_A_LAS_18, _1_HORA, _2_TURNOS);
		
		Agenda agenda = new Agenda("Agenda", ANTELACION_2_MESES);
		agenda.agregarJornadaLaboralHabitual(jornadaHabitual);
		
		SortedSet<Horario> horariosObtenidos = FiltroDeHorarios.ejecutar(jornadaHabitual.getHorarios(), _16_ENE_2012_A_LAS_10, _16_ENE_2012_A_LAS_17_30);
		assertEquals(8, horariosObtenidos.size());
	}

	/*--------------------------------------------------------------------------------------*/
	
}
