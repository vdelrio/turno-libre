package com.turnolibre.business.prestador;

import org.joda.time.DateTime;

/**
 * Una noticia que quiere comunicar un prestador de servicios determinado.
 *
 * @author Victor Del Rio
 */
public class Noticia implements Comparable<Noticia> {

	private Long id;
	
	private DateTime fecha;
	private String cuerpo;

	
	/*------------------------------------ Constructors ------------------------------------*/

	protected Noticia() {
		super();
	}
	
	public Noticia(DateTime fecha, String cuerpo) {
		super();
		this.fecha = fecha;
		this.cuerpo = cuerpo;
	}
	
	public Noticia(String cuerpo) {
		this(DateTime.now(), cuerpo);
	}

	/*--------------------------------------------------------------------------------------*/
	/*---------------------------------- Geters and seters ---------------------------------*/

	public Long getId() {
		return id;
	}

	public DateTime getFecha() {
		return fecha;
	}

	public String getCuerpo() {
		return cuerpo;
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
		if (!(obj instanceof Noticia))
			return false;
		Noticia other = (Noticia) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Noticia otra) {
		return this.getId().compareTo(otra.getId());
	}

	/*--------------------------------------------------------------------------------------*/
	
}
