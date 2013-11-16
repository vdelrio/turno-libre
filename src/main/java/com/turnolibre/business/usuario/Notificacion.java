package com.turnolibre.business.usuario;

import com.turnolibre.business.i18n.MensajeLocalizable;
import org.joda.time.DateTime;

/**
 * Notificaciones que envia el sistema a los usuarios cuando ocurren determinados eventos 
 * como cancelación de o cercanía de turnos.
 *
 * @author Victor Del Rio
 */
public class Notificacion implements Comparable<Notificacion> {

	private Long id;
	
	private DateTime fecha;
	private MensajeLocalizable mensaje;
	
	
	/*------------------------------------ Constructors ------------------------------------*/

	protected Notificacion() {
		super();
	}
	
	public Notificacion(MensajeLocalizable mensaje) {
		
		this.fecha = new DateTime();
		this.mensaje = mensaje;
	}

	/*--------------------------------------------------------------------------------------*/
	/*---------------------------------- Geters and seters ---------------------------------*/

	public Long getId() {
		return id;
	}
	
	public DateTime getFecha() {
		return fecha;
	}

	public MensajeLocalizable getMensaje() {
		return mensaje;
	}

	/*--------------------------------------------------------------------------------------*/
	/*-------------------------------- Hash code and equals --------------------------------*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Notificacion))
			return false;
		Notificacion other = (Notificacion) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public int compareTo(Notificacion otra) {
		return this.getId().compareTo(otra.getId());
	}

	/*--------------------------------------------------------------------------------------*/
	
}
