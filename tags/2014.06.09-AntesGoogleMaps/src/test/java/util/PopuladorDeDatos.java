package util;

import com.turnolibre.business.agenda.Agenda;
import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.prestador.PrestadorDeServicios;
import com.turnolibre.business.prestador.Rubro;
import com.turnolibre.business.ubicacion.Barrio;
import com.turnolibre.business.ubicacion.Ciudad;
import com.turnolibre.business.ubicacion.Provincia;
import com.turnolibre.business.ubicacion.Ubicacion;
import com.turnolibre.business.usuario.Usuario;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.LocalDate;

import java.util.Arrays;

import static util.TestConstants.*;


public class PopuladorDeDatos extends Populador {

	private static final String APPLICATION_CONTEXT = "classpath:spring/business-config.xml";
	private static final String SECURITY_CONTEXT = "classpath:spring/security-config.xml";


	private PopuladorDeDatos() {
		super(APPLICATION_CONTEXT, SECURITY_CONTEXT);
	}

	public static void main(String[] args) throws ExcepcionDeReglaDelNegocio {
		
		DateTimeUtils.setCurrentMillisFixed(LocalDate.now().withDayOfWeek(DateTimeConstants.MONDAY).toDateTimeAtStartOfDay().getMillis());
		
		PopuladorDeDatos populadorDeDatos = new PopuladorDeDatos();
		populadorDeDatos.ejecutar();
		
		DateTimeUtils.setCurrentMillisSystem();
	}
	
	private void ejecutar() throws ExcepcionDeReglaDelNegocio {
		
		popularProvincias();
		popularCiudades();
		popularBarrios();

		popularUsuarios();
		asignarRolesAdmin();

		popularRubros();
		popularPrestadores();

		popularAgendas();
		popularServicios();

		popularJornadasHabituales();
		popularDiasNoLaborales();
	}

	private void popularProvincias() {

		popularProvincia("CIUDAD AUTONOMA DE BUENOS AIRES");
		popularProvincia("BUENOS AIRES");
		popularProvincia("CATAMARCA");
		popularProvincia("CHACO");
		popularProvincia("CHUBUT");
		popularProvincia("CORDOBA");
		popularProvincia("CORRIENTES");
		popularProvincia("ENTRE RIOS");
		popularProvincia("FORMOSA");
		popularProvincia("JUJUY");
		popularProvincia("LA PAMPA");
		popularProvincia("LA RIOJA");
		popularProvincia("MENDOZA");
		popularProvincia("MISIONES");
		popularProvincia("NEUQUEN");
		popularProvincia("RIO NEGRO");
		popularProvincia("SALTA");
		popularProvincia("SAN JUAN");
		popularProvincia("SAN LUIS");
		popularProvincia("SANTA CRUZ");
		popularProvincia("SANTA FE");
		popularProvincia("SANTIAGO DEL ESTERO");
		popularProvincia("TIERRA DEL FUEGO");
		popularProvincia("TUCUMAN");
	}

	private void popularCiudades() {

		Provincia buenosAires = (Provincia) storedObjects.get("provincia - BUENOS AIRES");
		Provincia caba = (Provincia) storedObjects.get("provincia - CIUDAD AUTONOMA DE BUENOS AIRES");

		popularCiudad("CIUDAD AUTONOMA DE BUENOS AIRES", caba, null);
		popularCiudad("11 DE SEPTIEMBRE", buenosAires, "1657");
		popularCiudad("12 DE AGOSTO", buenosAires, "2701");
		popularCiudad("12 DE OCTUBRE", buenosAires, "6501");
	}

	private void popularBarrios() {
		popularBarrio("Colegiales", (Ciudad) storedObjects.get("ciudad - CIUDAD AUTONOMA DE BUENOS AIRES"));
	}
	
	private void popularUsuarios() throws ExcepcionDeReglaDelNegocio {
		
		Ciudad caba = (Ciudad) storedObjects.get("ciudad - CIUDAD AUTONOMA DE BUENOS AIRES");
		Ciudad onceSept = (Ciudad) storedObjects.get("ciudad - 11 DE SEPTIEMBRE");

		popularUsuario("Victor Del Rio",   "victor.delrio@gmail.com",    "123456", caba);
		popularUsuario("Manuel Rodriguez", "manuel.rodriguez@gmail.com", "123456", caba);
		popularUsuario("Juan Sanchez",     "juan.sanchez@gmail.com",     "123456", caba);
		popularUsuario("Carlos Del Rio",   "carlos.delrio@gmail.com",    "123456", caba);
		popularUsuario("German Ramirez",   "german.rodriguez@gmail.com", "123456", onceSept);
		popularUsuario("Alejandra Lopez",  "alejandra.lopez@gmail.com",  "123456", onceSept);
	}
	
	private void asignarRolesAdmin() throws ExcepcionDeReglaDelNegocio {
		
		asignarRolAdmin("German Ramirez");
		asignarRolAdmin("Alejandra Lopez");
		asignarRolAdmin("Carlos Del Rio");
		asignarRolAdmin("Victor Del Rio");
	}

	private void popularRubros() {

		popularRubro("Salud");
		popularRubro("Deportes");
		popularRubro("Belleza");
		popularRubro("Trámites");
	}

	private void popularPrestadores() throws ExcepcionDeReglaDelNegocio {

		Rubro rubroSalud = (Rubro) storedObjects.get("rubro - Salud");
		Ubicacion ubicacionPrestador1 = new Ubicacion((Ciudad) storedObjects.get("ciudad - CIUDAD AUTONOMA DE BUENOS AIRES"), (Barrio) storedObjects.get("barrio - Colegiales"), "Aguilar 2547");
		popularPrestador("Consultorio medico sur", ubicacionPrestador1, "4785-0055", "/images/prestadores/prestador1.jpg", rubroSalud);

		Rubro rubroDeportes = (Rubro) storedObjects.get("rubro - Deportes");
		Ubicacion ubicacionPrestador2 = new Ubicacion((Ciudad) storedObjects.get("ciudad - 11 DE SEPTIEMBRE"), "61 3476");
		popularPrestador("Futbol 5 Mentarios", ubicacionPrestador2, "4867-7455", "/images/prestadores/prestador2.jpg", rubroDeportes);
	}
	
	private void popularAgendas() throws ExcepcionDeReglaDelNegocio {
		
		popularAgenda((PrestadorDeServicios) storedObjects.get("prestador - Consultorio medico sur"), "Dr. Ramirez", ANTELACION_60_DIAS, Arrays.asList((Usuario) storedObjects.get("usuario - German Ramirez")));
		popularAgenda((PrestadorDeServicios) storedObjects.get("prestador - Consultorio medico sur"), "Dra. Lopez", ANTELACION_60_DIAS, Arrays.asList((Usuario) storedObjects.get("usuario - Alejandra Lopez")));
		popularAgenda((PrestadorDeServicios) storedObjects.get("prestador - Consultorio medico sur"), "Dr. Del Rio", ANTELACION_60_DIAS, Arrays.asList((Usuario) storedObjects.get("usuario - Carlos Del Rio")));
		popularAgenda((PrestadorDeServicios) storedObjects.get("prestador - Futbol 5 Mentarios"), "Canchas", ANTELACION_60_DIAS, Arrays.asList((Usuario) storedObjects.get("usuario - Victor Del Rio")));
	}

	private void popularServicios() {

		popularServicio((PrestadorDeServicios) storedObjects.get("prestador - Consultorio medico sur"), "Oftalmología", Arrays.asList((Agenda)storedObjects.get("agenda - Dr. Ramirez"), (Agenda)storedObjects.get("agenda - Dra. Lopez"), (Agenda)storedObjects.get("agenda - Dr. Del Rio")));
		popularServicio((PrestadorDeServicios) storedObjects.get("prestador - Consultorio medico sur"), "Cardiología", Arrays.asList((Agenda)storedObjects.get("agenda - Dra. Lopez")));
		popularServicio((PrestadorDeServicios) storedObjects.get("prestador - Futbol 5 Mentarios"), "Canchas de futbol", Arrays.asList((Agenda)storedObjects.get("agenda - Canchas")));
	}
	
	private void popularJornadasHabituales() throws ExcepcionDeReglaDelNegocio {
		
		popularJornadaHabitual((Agenda)storedObjects.get("agenda - Dr. Ramirez"), "lunes10a18_1", LUNES_A_LAS_10, LUNES_A_LAS_18, _1_HORA, _2_TURNOS);
		
		popularJornadaHabitual((Agenda)storedObjects.get("agenda - Dr. Del Rio"), "lunes10a18_2", LUNES_A_LAS_10, LUNES_A_LAS_18, _1_HORA, _2_TURNOS);
		popularJornadaHabitual((Agenda)storedObjects.get("agenda - Dr. Del Rio"), "martes930a1130", MARTES_A_LAS_10, MARTES_A_LAS_12, _20_MINUTOS, _1_TURNO);
		popularJornadaHabitual((Agenda)storedObjects.get("agenda - Dr. Del Rio"), "martes1430a1130", MARTES_A_LAS_14_30, MARTES_A_LAS_20_30, _1_30_HORAS, _2_TURNOS);
		popularJornadaHabitual((Agenda)storedObjects.get("agenda - Dr. Del Rio"), "miercoles10a18", MIERCOLES_A_LAS_10, MIERCOLES_A_LAS_18, _1_HORA, _1_TURNO);
		popularJornadaHabitual((Agenda)storedObjects.get("agenda - Dr. Del Rio"), "jueves10a18", JUEVES_A_LAS_10, JUEVES_A_LAS_18, _1_HORA, _1_TURNO);
		popularJornadaHabitual((Agenda)storedObjects.get("agenda - Dr. Del Rio"), "viernes10a18", VIERNES_A_LAS_10, VIERNES_A_LAS_18, _1_HORA, _2_TURNOS);
		popularJornadaHabitual((Agenda)storedObjects.get("agenda - Dr. Del Rio"), "sabados10a15", SABADO_A_LAS_10, SABADO_A_LAS_15, _1_HORA, _1_TURNO);
		
		popularJornadaHabitual((Agenda)storedObjects.get("agenda - Canchas"), "lunes9a18", LUNES_A_LAS_09, LUNES_A_LAS_18, _1_HORA, _3_TURNOS);
		popularJornadaHabitual((Agenda)storedObjects.get("agenda - Canchas"), "martes9a18", MARTES_A_LAS_09, MARTES_A_LAS_18, _1_HORA, _3_TURNOS);
		popularJornadaHabitual((Agenda)storedObjects.get("agenda - Canchas"), "miercoles9a18", MIERCOLES_A_LAS_09, MIERCOLES_A_LAS_18, _1_HORA, _3_TURNOS);
		popularJornadaHabitual((Agenda)storedObjects.get("agenda - Canchas"), "jueves9a18", JUEVES_A_LAS_09, JUEVES_A_LAS_18, _1_HORA, _3_TURNOS);
		popularJornadaHabitual((Agenda)storedObjects.get("agenda - Canchas"), "viernes9a18", VIERNES_A_LAS_09, VIERNES_A_LAS_18, _1_HORA, _3_TURNOS);
		popularJornadaHabitual((Agenda)storedObjects.get("agenda - Canchas"), "sabado9a13", SABADO_A_LAS_09, SABADO_A_LAS_13, _1_HORA, _3_TURNOS);
	}
	
	private void popularDiasNoLaborales() throws ExcepcionDeReglaDelNegocio {
		popularDiaNoLaboral((Agenda)storedObjects.get("agenda - Dr. Del Rio"), "diaConferencia", _29_DE_OCTUBRE_DEL_2014, "Décima conferencia de cardiología intervencionista");
	}

}
