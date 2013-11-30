package util;

import com.turnolibre.business.agenda.Agenda;
import com.turnolibre.business.agenda.DiaNoLaboral;
import com.turnolibre.business.agenda.JornadaLaboralHabitual;
import com.turnolibre.business.agenda.JornadaLaboralOcasional;
import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.joda.time.DayOfWeekTime;
import com.turnolibre.business.prestador.PrestadorDeServicios;
import com.turnolibre.business.prestador.Rubro;
import com.turnolibre.business.prestador.Servicio;
import com.turnolibre.business.turno.Horario;
import com.turnolibre.business.ubicacion.Barrio;
import com.turnolibre.business.ubicacion.Ciudad;
import com.turnolibre.business.ubicacion.Ubicacion;
import com.turnolibre.business.usuario.AdministradorDeAgenda;
import com.turnolibre.business.usuario.Cliente;
import com.turnolibre.business.usuario.Usuario;
import com.turnolibre.service.SharedService;
import org.joda.time.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.TestConstants.*;


public class PopuladorDeDatosDeTest {

	private static final String APPLICATION_CONTEXT = "classpath:spring/business-config.xml";
	private static final String SECURITY_CONTEXT = "classpath:spring/security-config.xml";

	private Map<String, Object> storedObjects = new HashMap<String, Object>();
	private SharedService sharedService;
	private PasswordEncoder passwordEncoder;

	
	public static void main(String[] args) throws ExcepcionDeReglaDelNegocio {
		
		DateTimeUtils.setCurrentMillisFixed(CURRENT_TIME_MOCK);

		PopuladorDeDatosDeTest populadorDeDatos = new PopuladorDeDatosDeTest();
		populadorDeDatos.ejecutar();

		DateTimeUtils.setCurrentMillisSystem();
	}

	private void ejecutar() throws ExcepcionDeReglaDelNegocio {

		inicializarDaos(APPLICATION_CONTEXT, SECURITY_CONTEXT);

		popularCiudades();
		popularBarrios();

		popularUsuarios();
		asignarRolesAdmin();

		popularRubros();
		popularPrestadores();

		popularAgendas();
		popularServicios();

		popularJornadasHabituales();
		popularJornadasOcasionales();
		popularDiasNoLaborales();

		sacarTurnos();
	}

	private void inicializarDaos(String... contexts) {

		ApplicationContext ctx = new ClassPathXmlApplicationContext(contexts);
		sharedService = (SharedService) ctx.getBean("sharedServiceImpl");
		passwordEncoder = (PasswordEncoder) ctx.getBean("passwordEncoder");
	}

	private void popularCiudades() {

		popularCiudad("CIUDAD AUTONOMA DE BUENOS AIRES", null, null);
		popularCiudad("11 DE SEPTIEMBRE", "1657", "BUENOS AIRES");
		popularCiudad("12 DE AGOSTO", "2701", "BUENOS AIRES");
		popularCiudad("12 DE OCTUBRE", "6501", "BUENOS AIRES");
	}

	private void popularBarrios() {
		popularBarrio("Colegiales", (Ciudad) storedObjects.get("ciudad - CIUDAD AUTONOMA DE BUENOS AIRES"));
	}

	private void popularUsuarios() throws ExcepcionDeReglaDelNegocio {

		popularUsuario("Victor Del Rio", "victor@gmail.com", "123456");
		popularUsuario("Manuel Rodriguez","manuel@gmail.com", "123456");
		popularUsuario("Juan Sanchez", "juan@gmail.com", "123456");
		popularUsuario("Carlos Del Rio", "carlos@gmail.com", "123456");
		popularUsuario("Admin Agenda1", "admin1@gmail.com", "123456");
		popularUsuario("Admin Agenda2", "admin2@gmail.com", "123456");
	}

	private void asignarRolesAdmin() throws ExcepcionDeReglaDelNegocio {

		asignarRolAdmin("Admin Agenda1");
		asignarRolAdmin("Admin Agenda2");
	}

	private void popularRubros() {

		popularRubro("Consultorios medicos");
	}

	private void popularPrestadores() throws ExcepcionDeReglaDelNegocio {

		Ubicacion ubicacionPrestador1 = new Ubicacion((Ciudad) storedObjects.get("ciudad - CIUDAD AUTONOMA DE BUENOS AIRES"), (Barrio) storedObjects.get("barrio - Colegiales"), "Aguilar 2547");
		popularPrestador("Consultorio medico sur", ubicacionPrestador1, "47850055", "/images/prestadores/prestador1.jpg", (Rubro) storedObjects.get("rubro - Consultorios medicos"));
	}

	private void popularAgendas() throws ExcepcionDeReglaDelNegocio {

		popularAgenda((PrestadorDeServicios) storedObjects.get("prestador - Consultorio medico sur"), "Dr. Ramirez", ANTELACION_2_MESES, Arrays.asList((Usuario) storedObjects.get("usuario - Admin Agenda1")));
		popularAgenda((PrestadorDeServicios) storedObjects.get("prestador - Consultorio medico sur"), "Dra. Lopez", ANTELACION_2_MESES, Arrays.asList((Usuario) storedObjects.get("usuario - Admin Agenda2")));
	}

	private void popularServicios() {

		popularServicio((PrestadorDeServicios) storedObjects.get("prestador - Consultorio medico sur"), "Oftalmología", Arrays.asList((Agenda)storedObjects.get("agenda - Dr. Ramirez"), (Agenda)storedObjects.get("agenda - Dra. Lopez")));
	}

	private void popularJornadasHabituales() throws ExcepcionDeReglaDelNegocio {

		popularJornadaHabitual((Agenda)storedObjects.get("agenda - Dr. Ramirez"), "lunes10a18_1", LUNES_A_LAS_10, LUNES_A_LAS_18, _1_HORA, _2_TURNOS);
		popularJornadaHabitual((Agenda)storedObjects.get("agenda - Dra. Lopez"), "lunes10a18_2", LUNES_A_LAS_10, LUNES_A_LAS_18, _1_HORA, _3_TURNOS);
	}

	private void popularJornadasOcasionales() throws ExcepcionDeReglaDelNegocio {

		popularJornadaOcasional((Agenda) storedObjects.get("agenda - Dra. Lopez"), "domingo18de10a18", DOM_18_MARZO_2012_A_LAS_10, DOM_18_MARZO_2012_A_LAS_17, _1_HORA, _2_TURNOS);
	}

	private void popularDiasNoLaborales() throws ExcepcionDeReglaDelNegocio {

		popularDiaNoLaboral((Agenda)storedObjects.get("agenda - Dra. Lopez"), "diaDelInvento", _20_DE_ENERO_DEL_2012, "Dia del invento");
		popularDiaNoLaboral((Agenda)storedObjects.get("agenda - Dra. Lopez"), "diaDeLaIndependencia", _9_DE_JULIO_DEL_2012, "Dia de la independencia");
		popularDiaNoLaboral((Agenda)storedObjects.get("agenda - Dra. Lopez"), "diaDelTrabajador", _1_DE_MAYO_DEL_2012, "Dia del trabajador");
	}

	private void sacarTurnos() throws ExcepcionDeReglaDelNegocio {

		sacarTurno((Cliente)((Usuario)storedObjects.get("usuario - Victor Del Rio")).getRol(Cliente.NOMBRE_DE_ROL), (Agenda)storedObjects.get("agenda - Dra. Lopez"), INTERVALO_EN_JORNADA_HABITUAL);
		sacarTurno((Cliente)((Usuario)storedObjects.get("usuario - Manuel Rodriguez")).getRol(Cliente.NOMBRE_DE_ROL), (Agenda)storedObjects.get("agenda - Dra. Lopez"), INTERVALO_EN_JORNADA_HABITUAL);
	}

	/*------------------------------------- Populadores ------------------------------------*/

	private void popularCiudad(String nombre, String codigoPostal, String provincia) {

		Ciudad ciudad = new Ciudad(nombre, codigoPostal, provincia);

		sharedService.save(ciudad);
		storedObjects.put("ciudad - " + nombre, ciudad);
	}

	private void popularBarrio(String nombre, Ciudad ciudad) {

		Barrio barrio = new Barrio(nombre, ciudad);

		sharedService.save(barrio);
		storedObjects.put("barrio - " + nombre, barrio);
	}

	private void popularUsuario(String nombre, String email, String password) throws ExcepcionDeReglaDelNegocio {

		Usuario usuario = new Usuario(nombre, email, passwordEncoder.encode(password));
		usuario.agregarRol(new Cliente());

		sharedService.save(usuario);
		storedObjects.put("usuario - " + nombre, usuario);
	}

	private void asignarRolAdmin(String nombreDeUsuario) throws ExcepcionDeReglaDelNegocio {

		Usuario usuario = (Usuario) storedObjects.get("usuario - " + nombreDeUsuario);
		usuario.agregarRol(new AdministradorDeAgenda());

		sharedService.update(usuario);
	}

	private void popularRubro(String nombre) {

		Rubro rubro = new Rubro(nombre);

		sharedService.save(rubro);
		storedObjects.put("rubro - " + nombre, rubro);
	}

	private void popularPrestador(String nombre, Ubicacion ubicacion, String telefono, String imagen, Rubro rubro) throws ExcepcionDeReglaDelNegocio {

		PrestadorDeServicios prestador = new PrestadorDeServicios(nombre, ubicacion, telefono, imagen);
		rubro.agregarPrestadorDeServicios(prestador);

		sharedService.save(prestador);
		storedObjects.put("prestador - " + nombre, prestador);
	}

	private void popularAgenda(PrestadorDeServicios prestadorDeServicios, String nombre, Period antelacionMaxima, List<Usuario> administradores) throws ExcepcionDeReglaDelNegocio {

		Agenda agenda = new Agenda(nombre, antelacionMaxima);
		prestadorDeServicios.agregarAgenda(agenda);

		sharedService.save(agenda);

		for (Usuario usuario : administradores) {

			((AdministradorDeAgenda) usuario.getRol(AdministradorDeAgenda.NOMBRE_DE_ROL)).getAgendas().add(agenda);
			sharedService.update(usuario);
		}

		storedObjects.put("agenda - " + nombre, agenda);
	}

	private void popularServicio(PrestadorDeServicios prestadorDeServicios, String nombre, List<Agenda> agendas) {

		Servicio servicio = new Servicio(nombre);

		for (Agenda agenda : agendas)
			servicio.getAgendas().add(agenda);

		prestadorDeServicios.agregarServicio(servicio);

		sharedService.save(servicio);
		storedObjects.put("servicio - " + nombre, servicio);
	}

	private void popularJornadaHabitual(Agenda agenda, String nombre, DayOfWeekTime diaYHoraDeInicio, DayOfWeekTime diaYHoraDeFin,
										Duration duracionDeLosTurnos, Integer vacantesPorTurno) throws ExcepcionDeReglaDelNegocio {

		JornadaLaboralHabitual jornadaHabitual = new JornadaLaboralHabitual(diaYHoraDeInicio, diaYHoraDeFin, duracionDeLosTurnos, vacantesPorTurno);
		agenda.agregarJornadaLaboralHabitual(jornadaHabitual);

		sharedService.update(agenda);
		storedObjects.put("jhabitual - " + nombre, jornadaHabitual);
	}

	private void popularJornadaOcasional(Agenda agenda, String nombre, DateTime desde, DateTime hasta,
										 Duration duracionDeLosTurnos, Integer vacantesPorTurno) throws ExcepcionDeReglaDelNegocio {

		JornadaLaboralOcasional jornadaOcasional = new JornadaLaboralOcasional(desde, hasta, duracionDeLosTurnos, vacantesPorTurno);
		agenda.agregarJornadaLaboralOcasional(jornadaOcasional);

		sharedService.update(agenda);
		storedObjects.put("jocasional - " + nombre, jornadaOcasional);
	}

	private void popularDiaNoLaboral(Agenda agenda, String nombre, LocalDate fecha, String motivo) throws ExcepcionDeReglaDelNegocio {

		DiaNoLaboral diaNoLaboral = new DiaNoLaboral(fecha, motivo);
		agenda.agregarDiaNoLaboral(diaNoLaboral);

		sharedService.save(diaNoLaboral);
		storedObjects.put("dnl - " + nombre, diaNoLaboral);
	}

	private void sacarTurno(Cliente cliente, Agenda agenda, Interval interval) throws ExcepcionDeReglaDelNegocio {

		Horario horarioASacarTurno = agenda.getHorario(interval);
		horarioASacarTurno.sacarTurno(cliente);

		sharedService.update(horarioASacarTurno);
	}

	/*--------------------------------------------------------------------------------------*/

}
