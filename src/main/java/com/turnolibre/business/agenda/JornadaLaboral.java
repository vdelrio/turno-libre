package com.turnolibre.business.agenda;

import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.i18n.MensajeLocalizable;
import com.turnolibre.business.joda.time.DateFormatter;
import com.turnolibre.business.turno.EstadoDeTurno;
import com.turnolibre.business.turno.Horario;
import com.turnolibre.business.turno.Turno;
import com.turnolibre.business.usuario.Cliente;
import com.turnolibre.business.usuario.Notificacion;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.util.*;

/**
 * Una jornada laboral define uno o más intervalos de tiempo en los cuales el prestador 
 * de servicios atiende turnos de manera ininterrumpida.
 *
 * @author Victor Del Rio
 */
public abstract class JornadaLaboral {

	private Long id;
	protected Agenda agenda;
	
	protected Duration duracionDeLosTurnos;
	protected Integer vacantesPorTurno;
	
	
	/*------------------------------------ Constructores -----------------------------------*/

	protected JornadaLaboral() {
		super();
	}

	public JornadaLaboral(Duration duracionDeLosTurnos, Integer vacantesPorTurno) {

		this.duracionDeLosTurnos = duracionDeLosTurnos;
		this.vacantesPorTurno = vacantesPorTurno;
	}
	
	/*--------------------------------------------------------------------------------------*/
	/*------------------------------------ Public methods ----------------------------------*/
	
	public SortedSet<Turno> getTurnos(EstadoDeTurno estado) {

		SortedSet<Turno> turnos = new TreeSet<Turno>();

		for (Horario horario : this.getHorarios()) {
			turnos.addAll(horario.getTurnos(estado));
		}

		return turnos;
	}

	public void notificarALosClientes(MensajeLocalizable mensaje) {

		for (Horario horario : this.getHorarios()) {
			horario.notificarALosClientes(mensaje);
		}
	}

	public void modificarDuracionDeLosTurnos(Duration nuevaDuracion) throws ExcepcionDeReglaDelNegocio {

		if (!this.duracionDeLosTurnos.equals(nuevaDuracion)) {

			validarDuracionMenorALaPermitida(nuevaDuracion);
			validarDuracionNoCoherenteConJornada(nuevaDuracion);

			this.duracionDeLosTurnos = nuevaDuracion;
			this.regenerarHorariosSegunDuracion();
		}
	}

	public void modificarVacantesPorTurno(Integer vacantes, String motivo) throws ExcepcionDeReglaDelNegocio {

		int diferenciaEnVacantes = vacantes - this.getVacantesPorTurno();

		if (diferenciaEnVacantes > 0)
			this.agregarVacantesPorTurno(diferenciaEnVacantes);
		else if (diferenciaEnVacantes < 0)
			this.quitarVacantesPorTurno(-diferenciaEnVacantes, motivo);
	}

	public void agregarVacantesPorTurno(Integer vacantesAAgregar) throws ExcepcionDeReglaDelNegocio {

		for (Horario horario : this.getHorarios())
			horario.agregarTurnos(vacantesAAgregar);

		this.vacantesPorTurno += vacantesAAgregar;
	}

	public void quitarVacantesPorTurno(Integer vacantesAQuitar, String motivo) throws ExcepcionDeReglaDelNegocio {

		for (Horario horario : this.getHorarios())
			horario.quitarTurnos(vacantesAQuitar, motivo);

		this.vacantesPorTurno -= vacantesAQuitar;
	}
	
	/*--------------------------------------------------------------------------------------*/
	/*----------------------------------- Abstract methods ---------------------------------*/
	
	public abstract SortedSet<Horario> getHorarios();

	public abstract void actualizarHorarios() throws ExcepcionDeReglaDelNegocio;

	protected abstract Duration getDuracionDeLosIntervalos();

	protected abstract void ligarA(Agenda agenda) throws ExcepcionDeReglaDelNegocio;

	protected abstract void desligarDe(Agenda agenda, String motivo) throws ExcepcionDeReglaDelNegocio;
	
	/*--------------------------------------------------------------------------------------*/
	/*---------------------------------- Geters and seters ---------------------------------*/

	public Long getId() {
		return id;
	}
	
	public Agenda getAgenda() {
		return agenda;
	}
	
	public Duration getDuracionDeLosTurnos() {
		return duracionDeLosTurnos;
	}

	public Integer getVacantesPorTurno() {
		return vacantesPorTurno;
	}

	/*--------------------------------------------------------------------------------------*/
	/*---------------------------------- Protected methods ---------------------------------*/

	protected void borrarHorarios(String motivo) {

		Turno.cancelarTurnos(this.getTurnos(EstadoDeTurno.OCUPADO), motivo);
		this.agenda.getHorarios().removeAll(this.getHorarios());
	}

	protected boolean estaLigada() {
		return this.agenda != null;
	}

	/*--------------------------------------------------------------------------------------*/
	/*----------------------------------- Private methods ----------------------------------*/
	
	private void regenerarHorariosSegunDuracion() throws ExcepcionDeReglaDelNegocio {

		Map<DateTime, List<Cliente>> turnosAnteriores = this.salvarTurnosActules();
		Agenda agenda = this.agenda;

		this.desligarDe(this.agenda, null);
		this.ligarA(agenda);

		this.reubicarClientes(turnosAnteriores);
	}

	private void reubicarClientes(Map<DateTime, List<Cliente>> turnosAnteriores) {

		for (Map.Entry<DateTime, List<Cliente>> entry : turnosAnteriores.entrySet()) {

			DateTime inicioDelTurno = entry.getKey();
			Interval intervalo = new Interval(inicioDelTurno, this.duracionDeLosTurnos);
			Horario horario = this.agenda.getHorario(intervalo);

			// Sigue existiendo el mismo comienzo de horario
			if (horario != null) {

				try {
					for (Cliente cliente : entry.getValue()) {

						horario.sacarTurno(cliente);
						notificarCambioDuracionTurno(cliente, inicioDelTurno);
					}
				} catch (ExcepcionDeReglaDelNegocio e) {}
			}
			else {
				for (Cliente cliente : entry.getValue())
					notificarCancelacionTurnoPorCambioDuracionTurnos(cliente, inicioDelTurno);
			}
		}
	}

	private Map<DateTime, List<Cliente>> salvarTurnosActules() {

		Map<DateTime, List<Cliente>> clientes = new TreeMap<DateTime, List<Cliente>>();

		for (Horario horario : this.getHorarios()) {

			List<Cliente> clientesEnHorario = horario.getClientes();
			if (!clientesEnHorario.isEmpty())
				clientes.put(horario.getIntervalo().getStart(), clientesEnHorario);
		}

		return clientes;
	}

	private void notificarCancelacionTurnoPorCambioDuracionTurnos(Cliente cliente, DateTime dateTime) {

		String fechaDelTurno = DateFormatter.formatDateTime(dateTime, true);
		String nombreDelPrestador = this.agenda.getPrestadorDeServicios().getNombre();
		String nombreDeLaAgenda = this.agenda.getNombre();

		cliente.getUsuario().getNotificaciones().add(
				new Notificacion(
						new MensajeLocalizable(
							"notificacion.cancelacion.turno.por.cambio.duracion.turnos",
							Arrays.asList(fechaDelTurno, nombreDelPrestador, nombreDeLaAgenda)
						)
				)
		);
	}

	private void notificarCambioDuracionTurno(Cliente cliente, DateTime dateTime) {

		String nombreDelPrestador = this.agenda.getPrestadorDeServicios().getNombre();
		String nombreDeLaAgenda = this.agenda.getNombre();
		String fechaDelTurno = DateFormatter.formatDateTime(dateTime, true);
		String duracionDeLosTurnos = DateFormatter.formatDuration(this.duracionDeLosTurnos);

		cliente.getUsuario().getNotificaciones().add(
				new Notificacion(
					new MensajeLocalizable(
							"notificacion.cambio.duracion.turno",
							Arrays.asList(fechaDelTurno, nombreDelPrestador, nombreDeLaAgenda, duracionDeLosTurnos)
					)
				)
		);
	}

    /*--------------------------------------------------------------------------------------*/
    /*----------------------------------- Business rules -----------------------------------*/

	private void validarDuracionMenorALaPermitida(Duration duracionDelHorario) throws ExcepcionDeReglaDelNegocio {

		if (duracionDelHorario.isShorterThan(Horario.getDuracionMinima()))
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.modificar.duracion.horarios.duracion.menor.a.la.permitida"));

	}

	public void validarDuracionNoCoherenteConJornada(Duration duracionDelTurno) throws ExcepcionDeReglaDelNegocio {

		long minutosQueDuraElTurno = duracionDelTurno.getStandardMinutes();
		long minutosQueDuraLaJornada = this.getDuracionDeLosIntervalos().getStandardMinutes();

		if (minutosQueDuraElTurno > minutosQueDuraLaJornada || minutosQueDuraLaJornada % minutosQueDuraElTurno != 0)
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.modificar.duracion.horarios.duracion.no.coherente.con.jornada"));
	}

	protected void validarjornadaYaLigada() throws ExcepcionDeReglaDelNegocio {

		if (this.estaLigada())
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.ligar.jornada.jornada.ya.ligada"));
	}

	protected void validarJornadaNoLigada() throws ExcepcionDeReglaDelNegocio {

		if (!this.estaLigada())
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.desligar.jornada.jornada.no.ligada"));
	}

	protected void validarJornadaYAgendaNoLigadas(Agenda agenda) throws ExcepcionDeReglaDelNegocio {

		if (!this.agenda.equals(agenda))
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.desligar.jornada.jornada.y.agenda.no.ligadas"));
	}

    /*--------------------------------------------------------------------------------------*/
	
}
