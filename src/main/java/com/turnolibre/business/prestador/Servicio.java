package com.turnolibre.business.prestador;

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

	private String nombre;
	private Set<PrestadorDeServicios> prestadoresDeServicios = new HashSet<>();


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

	public Set<PrestadorDeServicios> getPrestadoresDeServicios() {
		return prestadoresDeServicios;
	}

	public void setPrestadoresDeServicios(Set<PrestadorDeServicios> prestadoresDeServicios) {
		this.prestadoresDeServicios = prestadoresDeServicios;
	}

	/*--------------------------------------------------------------------------------------*/
	/*-------------------------------- Hash code and equals --------------------------------*/

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof Servicio))
			return false;

		Servicio servicio = (Servicio) obj;

		if (this.getNombre() != null)
			return this.getNombre().equals(servicio.getNombre());
		else
			return servicio.getNombre() == null;
	}

	@Override
	public int hashCode() {
		return this.getNombre() != null ? this.getNombre().hashCode() : 0;
	}

	@Override
	public int compareTo(Servicio otro) {
		return this.getNombre().compareTo(otro.getNombre());
	}

	/*--------------------------------------------------------------------------------------*/
	
}
