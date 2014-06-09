package com.turnolibre.business.turno;

import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.i18n.MensajeLocalizable;
import com.turnolibre.business.joda.time.DateFormatter;
import com.turnolibre.business.usuario.AdministradorDeAgenda;
import com.turnolibre.business.usuario.Cliente;
import com.turnolibre.business.usuario.Notificacion;

import java.util.SortedSet;

/**
 * Un turno es la unidad mínima de servicio que puede ser consumida por un único cliente. 
 * El turno se ofrece en un horario determinado y se encuentra en un estado determinado.
 *
 * @author Victor Del Rio
 */
public class Turno implements Comparable<Turno> {

	private Long id;
	
	private Horario horario;
	private Integer numero;
	private EstadoDeTurno estado;
	private String comentarioDelEstado;          // Informacion para el cliente
	private Cliente cliente;

	
	/*------------------------------------ Static methods ----------------------------------*/

	public static void cancelarTurnos(SortedSet<Turno> turnos, String motivo) {
		for (Turno turno : turnos)
			turno.cancelar(motivo);
	}

	public static void deshabilitarTurnos(SortedSet<Turno> turnos, String motivo) {
		for (Turno turno : turnos)
			turno.deshabilitar(motivo);
	}

	public static void habilitarTurnos(SortedSet<Turno> turnos) {
		for (Turno turno : turnos)
			turno.habilitar();
	}

	/*--------------------------------------------------------------------------------------*/
	/*------------------------------------ Constructors ------------------------------------*/

	protected Turno() {
		super();
	}
	
	public Turno(Horario horario, Integer numero) {
		
		this.horario = horario;
		this.numero = numero;
		this.estado = EstadoDeTurno.LIBRE;
	}

	/*--------------------------------------------------------------------------------------*/
	/*------------------------------------ Public methods ----------------------------------*/

	/**
	 * El cliente saca el turno.
	 */
	public synchronized void sacar(Cliente cliente) throws ExcepcionDeReglaDelNegocio {

		validarTurnoOcupado();
		validarMasDeUnTurnoPorHorario(cliente);

		this.cliente = cliente;
		this.estado = EstadoDeTurno.OCUPADO;
		cliente.getTurnos().add(this);
	}

	/**
	 * El administrador de la agenda cancela el turno.
	 */
	public synchronized void cancelar(String motivo) {

		if ( this.estaOcupado() ) {

			if (motivo != null)
				this.notificarCancelacionAlCliente(motivo);

			this.cliente.getTurnos().remove(this);
			this.cliente = null;
			this.estado = EstadoDeTurno.LIBRE;
		}
	}

	/**
	 * El cliente cancela un turno que tenía tomado.
	 */
	public synchronized void cancelar(Cliente cliente) throws ExcepcionDeReglaDelNegocio {

		validarClienteNoAutorizado(cliente);

		this.notificarCancelacionAlPrestador(cliente);
		this.cliente.getTurnos().remove(this);
		this.cliente = null;
		this.estado = EstadoDeTurno.LIBRE;
	}

	/**
	 * El administrador de la agenda deshabilita el turno.
	 */
	public synchronized void deshabilitar(String motivo) {

		if ( !this.estaDeshabilitado() ) {

			if ( this.estaOcupado() )
				this.notificarDeshabilitacionAlCliente(motivo);

			this.estado = EstadoDeTurno.DESHABILITADO;
			this.comentarioDelEstado = motivo;
		}
	}

	/**
	 * El administrador de la agenda habilita el turno.
	 */
	public synchronized void habilitar() {

		if ( this.estaDeshabilitado() ) {

			if ( this.cliente != null ) {
				this.notificarHabilitacionAlCliente();
				this.estado = EstadoDeTurno.OCUPADO;
			}
			else
				this.estado = EstadoDeTurno.LIBRE;

			this.comentarioDelEstado = null;
		}
	}

	public boolean estaOcupado() {
		return this.estado.equals(EstadoDeTurno.OCUPADO);
	}

	public boolean estaLibre() {
		return this.estado.equals(EstadoDeTurno.LIBRE);
	}

	public boolean estaDeshabilitado() {
		return this.estado.equals(EstadoDeTurno.DESHABILITADO);
	}

	/*--------------------------------------------------------------------------------------*/
    /*----------------------------------- Private methods ----------------------------------*/

	private void notificarCancelacionAlCliente(String motivo) {

		String nombreDelPrestador = this.horario.getAgenda().getPrestadorDeServicios().getNombre();
		String nombreDeLaAgenda = this.horario.getAgenda().getNombre();
		String fechaDelTurno = DateFormatter.formatDateTime(this.horario.getIntervalo().getStart(), true);

		this.cliente.getUsuario().getNotificaciones().add(
				new Notificacion(
					new MensajeLocalizable(
							"notificacion.cancelacion.por.prestador",
							fechaDelTurno, nombreDelPrestador, nombreDeLaAgenda, motivo
					)
				)
		);
	}

	private void notificarCancelacionAlPrestador(Cliente cliente) {

		String nombreDeLaAgenda = this.horario.getAgenda().getNombre();
		String fechaDelTurno = DateFormatter.formatDateTime(this.horario.getIntervalo().getStart(), true);
		String nombreDelCliente = cliente.getUsuario().getNombre();

		for (AdministradorDeAgenda administrador: this.horario.getAgenda().getAdministradores()) {
			administrador.getUsuario().getNotificaciones().add(
					new Notificacion(
							new MensajeLocalizable(
									"notificacion.cancelacion.por.cliente",
									fechaDelTurno, nombreDeLaAgenda, nombreDelCliente
							)
					)
			);
		}
	}

	private void notificarDeshabilitacionAlCliente(String motivo) {

		String nombreDelPrestador = this.horario.getAgenda().getPrestadorDeServicios().getNombre();
		String nombreDeLaAgenda = this.horario.getAgenda().getNombre();
		String fechaDelTurno = DateFormatter.formatDateTime(this.horario.getIntervalo().getStart(), true);

		this.cliente.getUsuario().getNotificaciones().add(
				new Notificacion(
						new MensajeLocalizable(
							"notificacion.deshabilitacion.por.prestador",
							fechaDelTurno, nombreDelPrestador, nombreDeLaAgenda, motivo
						)
				)
		);
	}

	private void notificarHabilitacionAlCliente() {

		String nombreDelPrestador = this.horario.getAgenda().getPrestadorDeServicios().getNombre();
		String nombreDeLaAgenda = this.horario.getAgenda().getNombre();
		String fechaDelTurno = DateFormatter.formatDateTime(this.horario.getIntervalo().getStart(), true);

		this.cliente.getUsuario().getNotificaciones().add(
				new Notificacion(
						new MensajeLocalizable(
							"notificacion.habilitacion.por.prestador",
							fechaDelTurno, nombreDelPrestador, nombreDeLaAgenda
						)
				)
		);
	}

    /*--------------------------------------------------------------------------------------*/
    /*----------------------------------- Business rules -----------------------------------*/

	private void validarTurnoOcupado() throws IllegalStateException {

		if (!this.estaLibre())
			throw new IllegalStateException();
	}

	private void validarMasDeUnTurnoPorHorario(Cliente cliente) throws ExcepcionDeReglaDelNegocio {

		for (Turno turnoDelCliente: cliente.getTurnos()) {

			if (turnoDelCliente.horario.equals(this.horario))
				throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.sacar.turno.mas.de.un.turno.por.horario"));
		}
	}

	private void validarClienteNoAutorizado(Cliente cliente) throws ExcepcionDeReglaDelNegocio {

		if (!this.cliente.equals(cliente))
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.cancelar.turno.cliente.no.autorizado"));
	}

    /*--------------------------------------------------------------------------------------*/
	/*---------------------------------- Geters and seters ---------------------------------*/

	public Long getId() {
		return id;
	}
	
	public Horario getHorario() {
		return horario;
	}
	
	public void setHorario(Horario horario) {
		this.horario = horario;
	}
	
	public Integer getNumero() {
		return numero;
	}
	
	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public EstadoDeTurno getEstado() {
		return estado;
	}

	public void setEstado(EstadoDeTurno estado) {
		this.estado = estado;
	}
	
	public String getComentarioDelEstado() {
		return comentarioDelEstado;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	/*--------------------------------------------------------------------------------------*/
	/*-------------------------------- Hash code and equals --------------------------------*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getHorario() == null) ? 0 : getHorario().hashCode());
		result = prime * result + ((getNumero() == null) ? 0 : getNumero().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Turno))
			return false;
		Turno other = (Turno) obj;
		if (getHorario() == null) {
			if (other.getHorario() != null)
				return false;
		} else if (!getHorario().equals(other.getHorario()))
			return false;
		if (getNumero() == null) {
			if (other.getNumero() != null)
				return false;
		} else if (!getNumero().equals(other.getNumero()))
			return false;
		return true;
	}

	@Override
	public int compareTo(Turno otro) {
		
		if ( this.getHorario().compareTo(otro.getHorario()) < 0 )
			return -1;
		else if ( this.getHorario().compareTo(otro.getHorario()) > 0 )
			return 1;
		else
			return this.getNumero().compareTo(otro.getNumero());
	}
	
	/*--------------------------------------------------------------------------------------*/
	/*-------------------------------------- Overrides -------------------------------------*/
	
	@Override
	public String toString() {
		return "Numero: " + this.numero.toString() + " Estado: " + this.estado.toString() + " Horario: " + this.horario.toString();
	}

	/*--------------------------------------------------------------------------------------*/
	
}
