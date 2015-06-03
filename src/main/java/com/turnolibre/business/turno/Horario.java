package com.turnolibre.business.turno;

import com.turnolibre.Configuration;
import com.turnolibre.business.agenda.Agenda;
import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.i18n.MensajeLocalizable;
import com.turnolibre.business.usuario.Cliente;
import com.turnolibre.business.usuario.Notificacion;
import com.turnolibre.util.CollectionUtils;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.util.*;

/**
 * Un horario es un intervalo de tiempo en el cual se puede sacar turno. En un horario se puede ofrecer 
 * uno o más turnos dependiendo de las facilidades del profesional. A la cantidad de turnos de un horario 
 * se las llama también "cantidad de vacantes del turno".
 *
 * @author Victor Del Rio
 */
public class Horario implements Comparable<Horario> {

	private Long id;
	private Agenda agenda;
	
	private Interval intervalo;
	private String comentarioDelEstado;           // Usado como tooltip
	private SortedSet<Turno> turnos = new TreeSet<>();


	/*------------------------------------ Static methods ----------------------------------*/

	public static Duration getDuracionMinima() {
		return Configuration.getInstance().getDuracionMinimaDeHorarios();
	}

	/*--------------------------------------------------------------------------------------*/
	/*------------------------------------ Constructors ------------------------------------*/

	protected Horario() {
		super();
	}
	
	public Horario(Interval intervalo, Integer cantidadDeTurnos, Agenda agenda) throws ExcepcionDeReglaDelNegocio {

		validarDuracionMenorALaPermitida(intervalo);

		this.intervalo = intervalo;
		this.agregarTurnos(cantidadDeTurnos);
		this.agenda = agenda;
	}

	public Horario(Interval intervalo, int cantidadDeTurnos) throws ExcepcionDeReglaDelNegocio {
		this(intervalo, cantidadDeTurnos, null);
	}

	/*--------------------------------------------------------------------------------------*/
	/*------------------------------------ Public methods ----------------------------------*/

	public Turno sacarTurno(Cliente cliente) throws ExcepcionDeReglaDelNegocio {

		validarTurnoPasado();
		validarAntelacionMaximaNoRespetada();

		Turno turnoSacado = null;

		for (Turno turno : this.turnos) {

			try {
				turno.sacar(cliente);
				turnoSacado = turno;
				break;
			} catch (IllegalStateException e) {
				// El turno no estaba libre
			}
		}

		return turnoSacado;
	}

	public void cancelarTurnos(String motivo) {
		Turno.cancelarTurnos(this.turnos, motivo);
	}

	public void deshabilitarTurnos(String motivo) {
		Turno.deshabilitarTurnos(this.turnos, motivo);
		this.comentarioDelEstado = motivo;
	}

	public void habilitarTurnos() {
		Turno.habilitarTurnos(this.turnos);
		this.comentarioDelEstado = null;
	}

	public void agregarTurnos(Integer cantidadDeTurnos) throws ExcepcionDeReglaDelNegocio {

		validarCantidadNegativa(cantidadDeTurnos);

		int numeroDeTurnoActual = this.turnos.size() + 1;

		for (int i = numeroDeTurnoActual; i < numeroDeTurnoActual + cantidadDeTurnos; i++) {
			this.turnos.add(new Turno(this, i));
		}
	}

	public void quitarTurnos(Integer cantidadAQuitar, String motivo) throws ExcepcionDeReglaDelNegocio {

		validarCantidadIncorrecta(cantidadAQuitar);

		SortedSet<Turno> turnosAQuitar = new TreeSet<>();

		turnosAQuitar.addAll(this.getTurnosAQuitar(EstadoDeTurno.DESHABILITADO, cantidadAQuitar, motivo));
		turnosAQuitar.addAll(this.getTurnosAQuitar(EstadoDeTurno.LIBRE, cantidadAQuitar - turnosAQuitar.size(), motivo));

		SortedSet<Turno> turnosOcupadosAQuitar = this.getTurnosAQuitar(EstadoDeTurno.OCUPADO, cantidadAQuitar - turnosAQuitar.size(), motivo);
		Turno.cancelarTurnos(turnosOcupadosAQuitar, motivo);

		turnosAQuitar.addAll(turnosOcupadosAQuitar);
		this.turnos.removeAll(turnosAQuitar);

		this.corregirLosNumerosDeLosTurnos();
	}

	public SortedSet<Turno> getTurnos(EstadoDeTurno estado) {

		SortedSet<Turno> turnosConEstado = new TreeSet<>();

		for (Turno turno : this.turnos) {
			if ( turno.getEstado().equals(estado) )
				turnosConEstado.add(turno);
		}

		return turnosConEstado;
	}

	public Turno getTurno(Integer numero) {
		return CollectionUtils.get(this.getTurnos(), new Turno(this, numero));
	}

	public void notificarALosClientes(MensajeLocalizable mensaje) {

		for (Turno turno: this.getTurnos(EstadoDeTurno.OCUPADO)) {
			turno.getCliente().getUsuario().getNotificaciones().add(new Notificacion(mensaje));
		}
	}

	public boolean cumpleConAntelacionMaxima() {
		return this.getIntervalo().getStart().isBefore(this.getAgenda().getFechaLimiteParaSacarTurno());
	}

	public EstadoDeTurno getEstado() {

		EstadoDeTurno estadoDelHorario = EstadoDeTurno.LIBRE;

		// Si no hay ninguno libre
		if ( this.getTurnos(EstadoDeTurno.LIBRE).isEmpty() ) {

			// Si todos estan deshabilitados
			if ( this.getTurnos(EstadoDeTurno.DESHABILITADO).size() == this.turnos.size() )
				estadoDelHorario = EstadoDeTurno.DESHABILITADO;
			else // O todos ocupados o mezcla de ocupados y deshabilitados
				estadoDelHorario = EstadoDeTurno.OCUPADO;
		}

		return estadoDelHorario;
	}

	public void setEstado(EstadoDeTurno estado) {

		for (Turno turno : this.turnos)
			turno.setEstado(estado);
	}

	public List<Cliente> getClientes() {

		List<Cliente> clientes = new ArrayList<>();

		for (Turno turno : this.turnos) {
			if (turno.getCliente() != null)
				clientes.add(turno.getCliente());
		}
		return clientes;
	}
	
	/*--------------------------------------------------------------------------------------*/
	/*---------------------------------- Geters and seters ---------------------------------*/

	public Long getId() {
		return id;
	}

	public Agenda getAgenda() {

		if ( agenda == null )
			throw new IllegalStateException("el horario no pertenece a ninguna agenda");

		return agenda;
	}

	public Interval getIntervalo() {
		return intervalo;
	}

	public String getComentarioDelEstado() {

		String comentario = this.comentarioDelEstado;

		if ( this.getEstado().equals(EstadoDeTurno.LIBRE) )
			comentario = this.getTurnos(EstadoDeTurno.LIBRE).size() + " turnos libres";
		else if ( this.getEstado().equals(EstadoDeTurno.OCUPADO) )
			comentario = "No hay turnos libres";

		return comentario;
	}

	public SortedSet<Turno> getTurnos() {
		return turnos;
	}

	/*--------------------------------------------------------------------------------------*/
	/*----------------------------------- Private methods ----------------------------------*/

	private SortedSet<Turno> getTurnosAQuitar(EstadoDeTurno estado, int cantidadAQuitar, String motivo) {

		SortedSet<Turno> turnosAQuitar = new TreeSet<>();

		// Hago una copia en un list para poder iterarlo en sentido contrario de modo que se
		// si se elimina un turno de un cliente, sea del cliente que lo saco hace menos tiempo
		List<Turno> turnosAIterar = CollectionUtils.asList(this.turnos);
		ListIterator<Turno> itTurnos = turnosAIterar.listIterator(turnosAIterar.size());

		while ( turnosAQuitar.size() < cantidadAQuitar && itTurnos.hasPrevious() ) {

			Turno turno = itTurnos.previous();
			if ( turno.getEstado().equals(estado) )
				turnosAQuitar.add(turno);
		}

		return turnosAQuitar;
	}

	private void corregirLosNumerosDeLosTurnos() {

		int numeroActual = 1;
		for (Turno turno : this.turnos)
			turno.setNumero(numeroActual++);
	}
	
	/*--------------------------------------------------------------------------------------*/
	/*----------------------------------- Business rules -----------------------------------*/

	private void validarDuracionMenorALaPermitida(Interval intervaloDelHorario) throws ExcepcionDeReglaDelNegocio {

		if (intervaloDelHorario.toDuration().isShorterThan(getDuracionMinima()))
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.crear.horario.duracion.menor.a.la.permitida"));
	}

	private void validarCantidadNegativa(Integer cantidadDeTurnos) throws ExcepcionDeReglaDelNegocio {

		if (cantidadDeTurnos <= 0)
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.agregar.turnos.cantidad.negativa"));
	}

	private void validarTurnoPasado() throws ExcepcionDeReglaDelNegocio {

		if (this.intervalo.getEnd().isBeforeNow())
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.sacar.turno.turno.pasado"));
	}

	private void validarAntelacionMaximaNoRespetada() throws ExcepcionDeReglaDelNegocio {

		if (!this.cumpleConAntelacionMaxima())
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.sacar.turno.antelacion.maxima.no.respetada"));
	}

	private void validarCantidadIncorrecta(Integer cantidadAQuitar) throws ExcepcionDeReglaDelNegocio {

		if (cantidadAQuitar <= 0 || cantidadAQuitar >= this.turnos.size())
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.quitar.turnos.cantidad.incorrecta"));
	}

    /*--------------------------------------------------------------------------------------*/
	/*-------------------------------- Hash code and equals --------------------------------*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getAgenda() == null) ? 0 : getAgenda().hashCode());
		result = prime * result + ((getIntervalo() == null) ? 0 : getIntervalo().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Horario))
			return false;
		Horario other = (Horario) obj;
		if (getAgenda() == null) {
			if (other.getAgenda() != null)
				return false;
		} else if (!getAgenda().equals(other.getAgenda()))
			return false;
		if (getIntervalo() == null) {
			if (other.getIntervalo() != null)
				return false;
		} else if (!getIntervalo().equals(other.getIntervalo()))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Horario otro) {
		return this.getIntervalo().getStart().compareTo(otro.getIntervalo().getStart());
	}

	/*--------------------------------------------------------------------------------------*/
	/*-------------------------------------- Overrides -------------------------------------*/

	@Override
	public String toString() {
		return this.intervalo.toString();
	}

	/*--------------------------------------------------------------------------------------*/
	
}
