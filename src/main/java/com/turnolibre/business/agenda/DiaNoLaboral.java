package com.turnolibre.business.agenda;

import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.i18n.MensajeLocalizable;
import com.turnolibre.business.turno.Horario;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.SortedSet;

/**
 * Un dia no laboral es un día en el cual no se prestará servicio a pesar de que el 
 * prestador o profesional atienda ese día de la semana. Un dia no laboral podría deberse a
 * un feriado o a cualquier otra eventualidad del prestador de servicios o profesional.
 *
 * @author Victor Del Rio
 */
public class DiaNoLaboral implements Comparable<DiaNoLaboral> {

	private Long id;
	private Agenda agenda;
	
	private LocalDate fecha;
	private String motivo;
	
	
	/*------------------------------------ Constructors ------------------------------------*/

	protected DiaNoLaboral() {
		super();
	}
	
	public DiaNoLaboral(LocalDate fecha, String motivo) throws ExcepcionDeReglaDelNegocio {
		this.setFecha(fecha);
		this.motivo = motivo;
	}

	/*--------------------------------------------------------------------------------------*/
	/*------------------------------------ Public methods ----------------------------------*/

	public SortedSet<Horario> getHorarios() {

		// TODO i18n

		if (this.agenda == null)
			throw new IllegalStateException("el dia no laboral no esta asociado a ninguna agenda");

		DateTime inicioDelDiaNoLaboral = this.fecha.toDateTimeAtStartOfDay();
		DateTime finDelDiaNoLaboral = inicioDelDiaNoLaboral.plusDays(1);

		return FiltroDeHorarios.ejecutar(this.agenda.getHorarios(), inicioDelDiaNoLaboral, finDelDiaNoLaboral);
	}

	public void habilitar() {

		for (Horario horario : this.getHorarios())
			horario.deshabilitarTurnos(this.motivo);
	}

	public void deshabilitar() {

		for (Horario horario : this.getHorarios())
			horario.habilitarTurnos();
	}

	/*--------------------------------------------------------------------------------------*/
	/*---------------------------------- Geters and seters ---------------------------------*/

	public Long getId() {
		return id;
	}
	
	public Agenda getAgenda() {
		return agenda;
	}
	
	protected void setAgenda(Agenda agenda) {
		this.agenda = agenda;
	}
	
	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) throws ExcepcionDeReglaDelNegocio {

		validarFechaPasada(fecha);
		this.fecha = fecha;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	/*--------------------------------------------------------------------------------------*/
	/*----------------------------------- Business rules -----------------------------------*/

	private void validarFechaPasada(LocalDate fecha) throws ExcepcionDeReglaDelNegocio {

		if (fecha.isBefore(LocalDate.now()))
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.setear.fecha.fecha.pasada"));
	}

	/*--------------------------------------------------------------------------------------*/
	/*-------------------------------- Hash code and equals --------------------------------*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getAgenda() == null) ? 0 : getAgenda().hashCode());
		result = prime * result + ((getFecha() == null) ? 0 : getFecha().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DiaNoLaboral))
			return false;
		DiaNoLaboral other = (DiaNoLaboral) obj;
		if (getAgenda() == null) {
			if (other.getAgenda() != null)
				return false;
		} else if (!getAgenda().equals(other.getAgenda()))
			return false;
		if (getFecha() == null) {
			if (other.getFecha() != null)
				return false;
		} else if (!getFecha().equals(other.getFecha()))
			return false;
		return true;
	}

	@Override
	public int compareTo(DiaNoLaboral otro) {
		return this.getFecha().compareTo(otro.getFecha());
	}
	
	/*--------------------------------------------------------------------------------------*/
	/*-------------------------------------- Overrides -------------------------------------*/
		
	@Override
	public String toString() {
		return this.fecha.toString() + " - " + this.motivo;
	}

	/*--------------------------------------------------------------------------------------*/
	
}
