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
import com.turnolibre.business.ubicacion.Ubicacion;
import com.turnolibre.business.usuario.AdministradorDeAgenda;
import com.turnolibre.business.usuario.Cliente;
import com.turnolibre.business.usuario.Usuario;
import com.turnolibre.service.SharedService;
import org.joda.time.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Populador {

	private SharedService sharedService;
	private PasswordEncoder passwordEncoder;

	protected Map<String, Object> storedObjects = new HashMap<>();


	/*------------------------------------ Constructors ------------------------------------*/

	public Populador(String... contexts) {

		ApplicationContext ctx = new ClassPathXmlApplicationContext(contexts);
		sharedService = (SharedService) ctx.getBean("sharedServiceImpl");
		passwordEncoder = (PasswordEncoder) ctx.getBean("passwordEncoder");
	}

    /*--------------------------------------------------------------------------------------*/
    /*------------------------------------ Public methods ----------------------------------*/

	public void popularUsuario(String nombre, String email, String password, String direccion) throws ExcepcionDeReglaDelNegocio {

		Usuario usuario = new Usuario(nombre, email, passwordEncoder.encode(password), direccion);
		usuario.agregarRol(new Cliente());

		sharedService.save(usuario);
		storedObjects.put("usuario - " + nombre, usuario);
	}

	public void asignarRolAdmin(String nombreDeUsuario) throws ExcepcionDeReglaDelNegocio {

		Usuario usuario = (Usuario) storedObjects.get("usuario - " + nombreDeUsuario);
		usuario.agregarRol(new AdministradorDeAgenda());

		sharedService.update(usuario);
	}

	public void popularRubro(String nombre) {

		Rubro rubro = new Rubro(nombre);

		sharedService.save(rubro);
		storedObjects.put("rubro - " + nombre, rubro);
	}

	public void popularPrestador(String nombre, String url, Ubicacion ubicacion, String telefono, String imagen, Rubro rubro) throws ExcepcionDeReglaDelNegocio {

		PrestadorDeServicios prestador = new PrestadorDeServicios(nombre, url, ubicacion, telefono, imagen);
		rubro.agregarPrestadorDeServicios(prestador);

		sharedService.save(prestador);
		storedObjects.put("prestador - " + nombre, prestador);
	}

	public void popularAgenda(PrestadorDeServicios prestadorDeServicios, String nombre, Period antelacionMaxima, List<Usuario> administradores) throws ExcepcionDeReglaDelNegocio {

		Agenda agenda = new Agenda(nombre, antelacionMaxima);
		prestadorDeServicios.agregarAgenda(agenda);

		sharedService.save(agenda);

		for (Usuario usuario : administradores) {

			usuario.getRol(AdministradorDeAgenda.class).getAgendas().add(agenda);
			sharedService.update(usuario);
		}

		storedObjects.put("agenda - " + nombre, agenda);
	}

	public void popularServicio(PrestadorDeServicios prestadorDeServicios, String nombre, List<Agenda> agendas) throws ExcepcionDeReglaDelNegocio {

		Servicio servicio = new Servicio(nombre);
		prestadorDeServicios.agregarServicio(servicio);

		for (Agenda agenda : agendas) {
			agenda.agregarServicio(servicio);
		}

		sharedService.save(servicio);
		storedObjects.put("servicio - " + nombre, servicio);
	}

	public void popularJornadaHabitual(Agenda agenda, String nombre, DayOfWeekTime diaYHoraDeInicio, DayOfWeekTime diaYHoraDeFin,
										Duration duracionDeLosTurnos, Integer vacantesPorTurno) throws ExcepcionDeReglaDelNegocio {

		JornadaLaboralHabitual jornadaHabitual = new JornadaLaboralHabitual(diaYHoraDeInicio, diaYHoraDeFin, duracionDeLosTurnos, vacantesPorTurno);
		agenda.agregarJornadaLaboralHabitual(jornadaHabitual);

		sharedService.update(agenda);
		storedObjects.put("jhabitual - " + nombre, jornadaHabitual);
	}

	public void popularJornadaOcasional(Agenda agenda, String nombre, DateTime desde, DateTime hasta,
										 Duration duracionDeLosTurnos, Integer vacantesPorTurno) throws ExcepcionDeReglaDelNegocio {

		JornadaLaboralOcasional jornadaOcasional = new JornadaLaboralOcasional(desde, hasta, duracionDeLosTurnos, vacantesPorTurno);
		agenda.agregarJornadaLaboralOcasional(jornadaOcasional);

		sharedService.update(agenda);
		storedObjects.put("jocasional - " + nombre, jornadaOcasional);
	}

	public void popularDiaNoLaboral(Agenda agenda, String nombre, LocalDate fecha, String motivo) throws ExcepcionDeReglaDelNegocio {

		DiaNoLaboral diaNoLaboral = new DiaNoLaboral(fecha, motivo);
		agenda.agregarDiaNoLaboral(diaNoLaboral);

		sharedService.save(diaNoLaboral);
		storedObjects.put("dnl - " + nombre, diaNoLaboral);
	}

	public void sacarTurno(Cliente cliente, Agenda agenda, Interval interval) throws ExcepcionDeReglaDelNegocio {

		Horario horarioASacarTurno = agenda.getHorario(interval);
		horarioASacarTurno.sacarTurno(cliente);

		sharedService.update(horarioASacarTurno);
	}

    /*--------------------------------------------------------------------------------------*/

}
