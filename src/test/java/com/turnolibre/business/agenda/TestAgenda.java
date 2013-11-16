package com.turnolibre.business.agenda;

import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.turno.EstadoDeTurno;
import com.turnolibre.business.turno.Horario;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static util.TestConstants.*;

public class TestAgenda {


	/*--------------------------------- Test configuration ---------------------------------*/

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DateTimeUtils.setCurrentMillisFixed(CURRENT_TIME_MOCK);
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		DateTimeUtils.setCurrentMillisSystem();
	}

	/*--------------------------------------------------------------------------------------*/
	/*------------------------------------- Unit tests -------------------------------------*/
	
	@Test
	public void debeDevolverLaFechaLimiteParaSacarTurno() {
		
		Agenda agenda = new Agenda("Agenda", PERIODO_1_SEMANA);
		DateTime fecha = agenda.getFechaLimiteParaSacarTurno();
		
		assertTrue(JUE_19_ENE_2012_A_LAS_17.isEqual(fecha));
	}
	
	@Test
	public void debeAgregarDiaNoLaboral() throws ExcepcionDeReglaDelNegocio {
		
		Agenda agenda = new Agenda("Agenda", PERIODO_1_SEMANA);
		JornadaLaboralOcasional jornadaOcasional = new JornadaLaboralOcasional(DOM_18_MARZO_2012_A_LAS_10, DOM_18_MARZO_2012_A_LAS_17, _1_HORA, _2_TURNOS);
		agenda.agregarJornadaLaboralOcasional(jornadaOcasional);
		
		for (Horario horario : jornadaOcasional.getHorarios())
			assertEquals(EstadoDeTurno.LIBRE, horario.getEstado());
		
		agenda.agregarDiaNoLaboral(new DiaNoLaboral(_18_DE_MARZO_DEL_2012, "Dia de la pachorra"));
		
		for (Horario horario : jornadaOcasional.getHorarios()) 
			assertEquals(EstadoDeTurno.DESHABILITADO, horario.getEstado());
	}
	
	/*--------------------------------------------------------------------------------------*/
	
}
