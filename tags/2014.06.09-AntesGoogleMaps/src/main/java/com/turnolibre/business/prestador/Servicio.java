package com.turnolibre.business.prestador;

import com.turnolibre.business.agenda.Agenda;

import java.util.HashSet;
import java.util.Set;

/**
 * Servicios que ofrecen los prestadores de servicios.
 * Ej.: oftalmología, cardiología, canchas de tennis, masages, etc.
 *
 * @author Victor Del Rio
 */
public class Servicio implements Comparable<Servicio> {

	private Long id;
	
	private PrestadorDeServicios prestadorDeServicios;
	private String nombre;

	private Set<Agenda> agendas = new HashSet<Agenda>();
	
	
	/*------------------------------------ Constructors ------------------------------------*/

	protected Servicio() {
		super();
	}
	
	public Servicio(String nombre) {
		this.nombre = nombre;
	}

	/*--------------------------------------------------------------------------------------*/
	/*---------------------------------- Geters and seters ---------------------------------*/

	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public PrestadorDeServicios getPrestadorDeServicios() {
		return prestadorDeServicios;
	}
	
	protected void setPrestadorDeServicios(PrestadorDeServicios prestadorDeServicios) {
		this.prestadorDeServicios = prestadorDeServicios;
	}

	public Set<Agenda> getAgendas() {
		return agendas;
	}

	/*--------------------------------------------------------------------------------------*/
	/*-------------------------------- Hash code and equals --------------------------------*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getNombre() == null) ? 0 : getNombre().hashCode());
		result = prime * result	+ ((getPrestadorDeServicios() == null) ? 0 : getPrestadorDeServicios().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Servicio))
			return false;
		Servicio other = (Servicio) obj;
		if (getNombre() == null) {
			if (other.getNombre() != null)
				return false;
		} else if (!getNombre().equals(other.getNombre()))
			return false;
		if (getPrestadorDeServicios() == null) {
			if (other.getPrestadorDeServicios() != null)
				return false;
		} else if (!getPrestadorDeServicios().equals(other.getPrestadorDeServicios()))
			return false;
		return true;
	}

	@Override
	public int compareTo(Servicio otro) {
		return this.getNombre().compareTo(otro.getNombre());
	}

	/*--------------------------------------------------------------------------------------*/
	
}
