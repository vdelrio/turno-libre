package com.turnolibre.aceptacion;

import com.turnolibre.business.agenda.DiaNoLaboral;
import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.turno.EstadoDeTurno;
import com.turnolibre.business.turno.Horario;
import com.turnolibre.service.AgendaService;
import com.turnolibre.service.JornadaLaboralOcasionalService;
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
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static org.junit.Assert.*;
import static util.TestConstants.*;


/**
 * [US-002] Como prestador de servicios quiero definir días no laborales
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/business-config.xml", "classpath:spring/security-config.xml"})
@TransactionConfiguration
@Transactional
@ActiveProfiles("test")
public class PrestadorDefineDiasNoLaborales {

	@Autowired
	private AgendaService agendaService;
	@Autowired
	private SharedService sharedService;
	
	@Autowired
	private JornadaLaboralOcasionalService jornadaLaboralOcasionalService;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DateTimeUtils.setCurrentMillisFixed(CURRENT_TIME_MOCK);
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		DateTimeUtils.setCurrentMillisSystem();
	}
	
	
	/**
	 * [UAT-2.1] Alta de día laboral sin problemas
	 * DADA una agenda
	 * CUANDO agrego un día no laboral
	 * ENTONCES 
	 *  veo en la agenda que los turnos para ese día estan deshabilitados Y
	 *  que cundo me posiciono con el mouse sobre uno de sus turnos, me muestra un tooltip con el motivo del día no laboral
	 */
	@Test
	public void altaDeDiaLaboralSinProblemas() throws ExcepcionDeReglaDelNegocio {
		
		DiaNoLaboral diaNoLaboralAAgregar = new DiaNoLaboral(_25_DE_MAYO_DEL_2012, "Dia de la revolucion de mayo");
		Long diaNoLaboralId = agendaService.agregarDiaNoLaboral(AGENDA_DRA_LOPEZ_ID, diaNoLaboralAAgregar);
		
		DiaNoLaboral diaNoLaboral = sharedService.load(DiaNoLaboral.class, diaNoLaboralId);
		
		assertNotNull(diaNoLaboral.getId());
		assertEquals(_25_DE_MAYO_DEL_2012, diaNoLaboral.getFecha());
		assertEquals("Dia de la revolucion de mayo", diaNoLaboral.getMotivo());
	}

	/**
	 * [UAT-2.2] Alta de día laboral con turnos tomados
	 * DADA una agenda con turnos tomados para una determinada fecha
	 * CUANDO agrego un día no laboral para dicha fecha
	 * ENTONCES se deshabilitan todos los turnos para la fecha y se agrega el día no laboral
	 */
	@Test
	public void altaDeDiaLaboralConTurnosTomados() throws ExcepcionDeReglaDelNegocio {
		
		Collection<Horario> horariosDel18DeMarzo = jornadaLaboralOcasionalService.findHorarios(JORNADA_DEL_18_DE_MARZO_ID);
		
		for (Horario horario : horariosDel18DeMarzo)
			assertEquals(EstadoDeTurno.LIBRE, horario.getEstado());
		
		DiaNoLaboral diaNoLaboralAAgregar = new DiaNoLaboral(_18_DE_MARZO_DEL_2012, "Dia de la pachorra");
		Long diaNoLaboralId = agendaService.agregarDiaNoLaboral(AGENDA_DRA_LOPEZ_ID, diaNoLaboralAAgregar);
		
		DiaNoLaboral diaNoLaboral = sharedService.load(DiaNoLaboral.class, diaNoLaboralId);
		
		assertNotNull(diaNoLaboral.getId());
		assertEquals(_18_DE_MARZO_DEL_2012, diaNoLaboral.getFecha());
		assertEquals("Dia de la pachorra", diaNoLaboral.getMotivo());
		
		horariosDel18DeMarzo = jornadaLaboralOcasionalService.findHorarios(JORNADA_DEL_18_DE_MARZO_ID);
		
		for (Horario horario : horariosDel18DeMarzo)
			assertEquals(EstadoDeTurno.DESHABILITADO, horario.getEstado());
	}
	
	/**
	 * [UAT-2.3] Alta de día laboral con fecha no futura
	 * DADA una agenda
	 * CUANDO intento agregar un día no laboral con fecha no futura
	 * ENTONCES el sistema no me deja realizar la operación
	 */
	@Test(expected=ExcepcionDeReglaDelNegocio.class)
	public void altaDeDiaLaboralConFechaNoFutura() throws ExcepcionDeReglaDelNegocio {
		
		DiaNoLaboral diaNoLaboralAAgregar = new DiaNoLaboral(_1_DE_ENERO_DEL_2012, "Dia de la pachorra nacional");
		agendaService.agregarDiaNoLaboral(AGENDA_DRA_LOPEZ_ID, diaNoLaboralAAgregar);
	}
	
	/**
	 * [UAT-2.4] Alta de día laboral con fecha ya no laboral
	 * DADA una agenda con un día no laboral para una determinada fecha
	 * CUANDO intento agregar otro día no laboral para la mísma fecha
	 * ENTONCES el sistema no me deja realizar la operación
	 */
	@Test(expected=ExcepcionDeReglaDelNegocio.class)
	public void altaDeDiaLaboralConFechaYaNoLaboral() throws ExcepcionDeReglaDelNegocio {
		
		DiaNoLaboral diaNoLaboralAAgregar = new DiaNoLaboral(_9_DE_JULIO_DEL_2012, "Dia de la independencia");
		agendaService.agregarDiaNoLaboral(AGENDA_DRA_LOPEZ_ID, diaNoLaboralAAgregar);
	}
	
	/**
	 * [UAT-2.5] Modificación de día laboral sin problemas
	 * DADA una agenda con un día no laboral para una determinada fecha
	 * CUANDO modifico dicho día no laboral
	 * ENTONCES veo reflejado el cambio tanto en la agenda como en el panel del administrador de la agenda
	 */
	@Test
	public void modificacionDeDiaLaboralSinProblemas() throws ExcepcionDeReglaDelNegocio {
		
		DiaNoLaboral diaNoLaboralAModificar = sharedService.load(DiaNoLaboral.class, _9_DE_JULIO_ID);
		DiaNoLaboral diaNoLaboralModificado = new DiaNoLaboral(_31_DE_DICIEMBRE_DEL_2012, "Motivo modificado");
		
		Long nuevoNueveDeJulioId = agendaService.modificarDiaNoLaboral(AGENDA_DRA_LOPEZ_ID, _9_DE_JULIO_ID, diaNoLaboralModificado);
				
		DiaNoLaboral diaNoLaboralObtenidoModificado = sharedService.load(DiaNoLaboral.class, nuevoNueveDeJulioId);
		
		assertEquals(nuevoNueveDeJulioId, diaNoLaboralObtenidoModificado.getId());
		assertEquals(_31_DE_DICIEMBRE_DEL_2012, diaNoLaboralObtenidoModificado.getFecha());
		assertEquals("Motivo modificado", diaNoLaboralObtenidoModificado.getMotivo());
	}
	
	/**
	 * [UAT-2.6] Modificación de día laboral con fecha no futura
	 * DADA una agenda con un día no laboral para una determinada fecha
	 * CUANDO intento modificar dicho día no laboral cambiando su fecha por una no futura
	 * ENTONCES el sistema no me deja realizar la operación
	 */
	@Test(expected=ExcepcionDeReglaDelNegocio.class)
	public void modificacionDeDiaLaboralConFechaNoFutura() throws ExcepcionDeReglaDelNegocio {
		
		DiaNoLaboral diaNoLaboralAModificar = sharedService.load(DiaNoLaboral.class, _9_DE_JULIO_ID);
		DiaNoLaboral diaNoLaboralModificado = new DiaNoLaboral(_1_DE_ENERO_DEL_2012, diaNoLaboralAModificar.getMotivo());
		
		agendaService.modificarDiaNoLaboral(AGENDA_DRA_LOPEZ_ID, _9_DE_JULIO_ID, diaNoLaboralModificado);
	}
	
	/**
	 * [UAT-2.7] Modificación de día laboral con fecha ya no laboral
	 * DADA una agenda con un día no laboral para una determinada fecha
	 * CUANDO intento modificar dicho día no laboral cambiando su fecha por la de otro día no laboral ya existente
	 * ENTONCES el sistema no me deja realizar la operación
	 */
	@Test(expected=ExcepcionDeReglaDelNegocio.class)
	public void modificacionDeDiaLaboralConFechaYaNoLaboral() throws ExcepcionDeReglaDelNegocio {
		
		DiaNoLaboral diaNoLaboralAModificar = sharedService.load(DiaNoLaboral.class, _9_DE_JULIO_ID);
		DiaNoLaboral diaNoLaboralModificado = new DiaNoLaboral(_1_DE_MAYO_DEL_2012, diaNoLaboralAModificar.getMotivo());
		
		agendaService.modificarDiaNoLaboral(AGENDA_DRA_LOPEZ_ID, _9_DE_JULIO_ID, diaNoLaboralModificado);
	}
	
	/**
	 * [UAT-2.8] Baja de día laboral sin problemas
	 * DADA una agenda con un día no laboral para una determinada fecha
	 * CUANDO borro dicho día no laboral
	 * ENTONCES 
	 *  veo en la agenda que los turnos estan nuevamente disponibles Y
	 * 	veo en el panel del administrador de la agenda que el día no laboral ya no existe
	 */	
	@Test
	public void bajaDeDiaNoLaboralSinProblemas() throws ExcepcionDeReglaDelNegocio {
		
		DiaNoLaboral diaDeLaIndependencia = sharedService.load(DiaNoLaboral.class, _9_DE_JULIO_ID);
		assertNotNull(diaDeLaIndependencia);
		
		agendaService.quitarDiaNoLaboral(AGENDA_DRA_LOPEZ_ID, _9_DE_JULIO_ID);

		Collection<DiaNoLaboral> diasNoLaborales = agendaService.findDiasNoLaborales(AGENDA_DRA_LOPEZ_ID);
		assertFalse(diasNoLaborales.contains(diaDeLaIndependencia));
	}
	
}
