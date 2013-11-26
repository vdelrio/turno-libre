package util;

import com.turnolibre.business.agenda.Agenda;
import com.turnolibre.business.agenda.DiaNoLaboral;
import com.turnolibre.business.agenda.JornadaLaboralHabitual;
import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.joda.time.DayOfWeekTime;
import com.turnolibre.business.prestador.PrestadorDeServicios;
import com.turnolibre.business.prestador.Rubro;
import com.turnolibre.business.prestador.Servicio;
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


public class PopuladorDeDatos {

	private static final String APPLICATION_CONTEXT = "classpath:spring/business-config.xml";
	private static final String SECURITY_CONTEXT = "classpath:spring/security-config.xml";

	private Map<String, Object> storedObjects = new HashMap<String, Object>();
	private SharedService sharedService;
	private PasswordEncoder passwordEncoder;

	
	public static void main(String[] args) throws ExcepcionDeReglaDelNegocio {
		
		DateTimeUtils.setCurrentMillisFixed(LocalDate.now().withDayOfWeek(DateTimeConstants.MONDAY).toDateTimeAtStartOfDay().getMillis());
		
		PopuladorDeDatos populadorDeDatos = new PopuladorDeDatos();
		populadorDeDatos.ejecutar();
		
		DateTimeUtils.setCurrentMillisSystem();
	}
	
	private void ejecutar() throws ExcepcionDeReglaDelNegocio {
		
		inicializarDaos(APPLICATION_CONTEXT, SECURITY_CONTEXT);

		popularUsuarios();
		asignarRolesAdmin();

		popularRubros();
		popularPrestadores();

		popularAgendas();
		popularServicios();

		popularJornadasHabituales();
		popularDiasNoLaborales();
	}
	
	private void inicializarDaos(String... contexts) {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext(contexts);
		sharedService = (SharedService) ctx.getBean("sharedServiceImpl");
		passwordEncoder = (PasswordEncoder) ctx.getBean("passwordEncoder");
	}
	
	private void popularUsuarios() throws ExcepcionDeReglaDelNegocio {
		
		popularUsuario("Victor Del Rio",   "victor.delrio@gmail.com",    "123456");
		popularUsuario("Manuel Rodriguez", "manuel.rodriguez@gmail.com", "123456");
		popularUsuario("Juan Sanchez",     "juan.sanchez@gmail.com",     "123456");
		popularUsuario("Carlos Del Rio",   "carlos.delrio@gmail.com",    "123456");
		popularUsuario("German Ramirez",   "german.rodriguez@gmail.com", "123456");
		popularUsuario("Alejandra Lopez",  "alejandra.lopez@gmail.com",  "123456");
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

		Rubro rubroSalud = (Rubro) storedObjects.get("Salud");
		popularPrestador("Consultorio medico sur", "4785-0055", "/images/prestadores/prestador1.jpg", rubroSalud);

		Rubro rubroDeportes = (Rubro) storedObjects.get("Deportes");
		popularPrestador("Futbol 5 Mentarios", "4867-7455", "/images/prestadores/prestador2.jpg", rubroDeportes);
	}
	
	private void popularAgendas() throws ExcepcionDeReglaDelNegocio {
		
		popularAgenda((PrestadorDeServicios) storedObjects.get("Consultorio medico sur"), "Dr. Ramirez", ANTELACION_60_DIAS, Arrays.asList((Usuario) storedObjects.get("German Ramirez")));
		popularAgenda((PrestadorDeServicios) storedObjects.get("Consultorio medico sur"), "Dra. Lopez", ANTELACION_60_DIAS, Arrays.asList((Usuario) storedObjects.get("Alejandra Lopez")));
		popularAgenda((PrestadorDeServicios) storedObjects.get("Consultorio medico sur"), "Dr. Del Rio", ANTELACION_60_DIAS, Arrays.asList((Usuario) storedObjects.get("Carlos Del Rio")));
		popularAgenda((PrestadorDeServicios) storedObjects.get("Futbol 5 Mentarios"), "Canchas", ANTELACION_60_DIAS, Arrays.asList((Usuario) storedObjects.get("Victor Del Rio")));
	}

	private void popularServicios() {

		popularServicio((PrestadorDeServicios) storedObjects.get("Consultorio medico sur"), "Oftalmología", Arrays.asList((Agenda)storedObjects.get("Dr. Ramirez"), (Agenda)storedObjects.get("Dra. Lopez"), (Agenda)storedObjects.get("Dr. Del Rio")));
		popularServicio((PrestadorDeServicios) storedObjects.get("Consultorio medico sur"), "Cardiología", Arrays.asList((Agenda)storedObjects.get("Dra. Lopez")));
		popularServicio((PrestadorDeServicios) storedObjects.get("Futbol 5 Mentarios"), "Canchas de futbol", Arrays.asList((Agenda)storedObjects.get("Canchas")));
	}
	
	private void popularJornadasHabituales() throws ExcepcionDeReglaDelNegocio {
		
		popularJornadaHabitual((Agenda)storedObjects.get("Dr. Ramirez"), "lunes10a18_1", LUNES_A_LAS_10, LUNES_A_LAS_18, _1_HORA, _2_TURNOS);
		
		popularJornadaHabitual((Agenda)storedObjects.get("Dr. Del Rio"), "lunes10a18_2", LUNES_A_LAS_10, LUNES_A_LAS_18, _1_HORA, _2_TURNOS);
		popularJornadaHabitual((Agenda)storedObjects.get("Dr. Del Rio"), "martes930a1130", MARTES_A_LAS_10, MARTES_A_LAS_12, _20_MINUTOS, _1_TURNO);
		popularJornadaHabitual((Agenda)storedObjects.get("Dr. Del Rio"), "martes1430a1130", MARTES_A_LAS_14_30, MARTES_A_LAS_20_30, _1_30_HORAS, _2_TURNOS);
		popularJornadaHabitual((Agenda)storedObjects.get("Dr. Del Rio"), "miercoles10a18", MIERCOLES_A_LAS_10, MIERCOLES_A_LAS_18, _1_HORA, _1_TURNO);
		popularJornadaHabitual((Agenda)storedObjects.get("Dr. Del Rio"), "jueves10a18", JUEVES_A_LAS_10, JUEVES_A_LAS_18, _1_HORA, _1_TURNO);
		popularJornadaHabitual((Agenda)storedObjects.get("Dr. Del Rio"), "viernes10a18", VIERNES_A_LAS_10, VIERNES_A_LAS_18, _1_HORA, _2_TURNOS);
		popularJornadaHabitual((Agenda)storedObjects.get("Dr. Del Rio"), "sabados10a15", SABADO_A_LAS_10, SABADO_A_LAS_15, _1_HORA, _1_TURNO);
		
		popularJornadaHabitual((Agenda)storedObjects.get("Canchas"), "lunes9a18", LUNES_A_LAS_09, LUNES_A_LAS_18, _1_HORA, _3_TURNOS);
		popularJornadaHabitual((Agenda)storedObjects.get("Canchas"), "martes9a18", MARTES_A_LAS_09, MARTES_A_LAS_18, _1_HORA, _3_TURNOS);
		popularJornadaHabitual((Agenda)storedObjects.get("Canchas"), "miercoles9a18", MIERCOLES_A_LAS_09, MIERCOLES_A_LAS_18, _1_HORA, _3_TURNOS);
		popularJornadaHabitual((Agenda)storedObjects.get("Canchas"), "jueves9a18", JUEVES_A_LAS_09, JUEVES_A_LAS_18, _1_HORA, _3_TURNOS);
		popularJornadaHabitual((Agenda)storedObjects.get("Canchas"), "viernes9a18", VIERNES_A_LAS_09, VIERNES_A_LAS_18, _1_HORA, _3_TURNOS);
		popularJornadaHabitual((Agenda)storedObjects.get("Canchas"), "sabado9a13", SABADO_A_LAS_09, SABADO_A_LAS_13, _1_HORA, _3_TURNOS);
	}
	
	private void popularDiasNoLaborales() throws ExcepcionDeReglaDelNegocio {
		popularDiaNoLaboral((Agenda)storedObjects.get("Dr. Del Rio"), "diaConferencia", _29_DE_OCTUBRE_DEL_2013, "Décima conferencia de cardiología intervencionista");
	}
	
	/*------------------------------------- Populadores ------------------------------------*/

	private void popularUsuario(String nombre, String email, String password) throws ExcepcionDeReglaDelNegocio {
		
		Usuario usuario = new Usuario(nombre, email, passwordEncoder.encode(password));
		usuario.agregarRol(new Cliente());

		sharedService.save(usuario);
		storedObjects.put(nombre, usuario);
	}
	
	private void asignarRolAdmin(String nombreDeUsuario) throws ExcepcionDeReglaDelNegocio {
		
		Usuario usuario = (Usuario) storedObjects.get(nombreDeUsuario);
		usuario.agregarRol(new AdministradorDeAgenda());

		sharedService.update(usuario);
	}

	private void popularRubro(String nombre) {

		Rubro rubro = new Rubro(nombre);

		sharedService.save(rubro);
		storedObjects.put(nombre, rubro);
	}

	private void popularPrestador(String nombre, String telefono, String imagen, Rubro rubro) throws ExcepcionDeReglaDelNegocio {

		PrestadorDeServicios prestador = new PrestadorDeServicios(nombre, telefono, imagen);
		rubro.agregarPrestadorDeServicios(prestador);

		sharedService.save(prestador);
		storedObjects.put(nombre, prestador);
	}
	
	private void popularAgenda(PrestadorDeServicios prestadorDeServicios, String nombre, Period antelacionMaxima, List<Usuario> administradores) throws ExcepcionDeReglaDelNegocio {
		
		Agenda agenda = new Agenda(nombre, antelacionMaxima);
		prestadorDeServicios.agregarAgenda(agenda);

		sharedService.save(agenda);

		for (Usuario usuario : administradores) {
			
			((AdministradorDeAgenda) usuario.getRol(AdministradorDeAgenda.NOMBRE_DE_ROL)).getAgendas().add(agenda);
			sharedService.update(usuario);
		}
		
		storedObjects.put(nombre, agenda);
	}

	private void popularServicio(PrestadorDeServicios prestadorDeServicios, String nombre, List<Agenda> agendas) {

		Servicio servicio = new Servicio(nombre);

		for (Agenda agenda : agendas)
			servicio.getAgendas().add(agenda);

		prestadorDeServicios.agregarServicio(servicio);

		sharedService.save(servicio);
		storedObjects.put(nombre, servicio);
	}
	
	private void popularJornadaHabitual(Agenda agenda, String nombre, DayOfWeekTime diaYHoraDeInicio, DayOfWeekTime diaYHoraDeFin,
			Duration duracionDeLosTurnos, Integer vacantesPorTurno) throws ExcepcionDeReglaDelNegocio {
		
		JornadaLaboralHabitual jornadaHabitual = new JornadaLaboralHabitual(diaYHoraDeInicio, diaYHoraDeFin, duracionDeLosTurnos, vacantesPorTurno);
		agenda.agregarJornadaLaboralHabitual(jornadaHabitual);

		sharedService.update(agenda);
		storedObjects.put(nombre, jornadaHabitual);
	}	
	
	private void popularDiaNoLaboral(Agenda agenda, String nombre, LocalDate fecha, String motivo) throws ExcepcionDeReglaDelNegocio {
		
		DiaNoLaboral diaNoLaboral = new DiaNoLaboral(fecha, motivo);
		agenda.agregarDiaNoLaboral(diaNoLaboral);

		sharedService.save(diaNoLaboral);
		storedObjects.put(nombre, diaNoLaboral);
	}
	
	/*--------------------------------------------------------------------------------------*/
	
}
