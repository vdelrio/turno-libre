package com.turnolibre.business.prestador;

import java.util.Set;
import java.util.TreeSet;

/**
 * Rubros a los cuales pueden aplicar los prestadores de servicios. 
 * Ej.: salud, deportes, etc.
 *
 * @author Victor Del Rio
 */
public class Rubro implements Comparable<Rubro> {

	private Long id;
	private String nombre;
	
	private Set<PrestadorDeServicios> prestadoresDeServicio = new TreeSet<PrestadorDeServicios>();
	
	
	/*------------------------------------ Constructors ------------------------------------*/

	protected Rubro() {
		super();
	}
	
	public Rubro(String nombre) {
		this.nombre = nombre;
	}

	/*--------------------------------------------------------------------------------------*/
	/*------------------------------------ Public methods ----------------------------------*/

	public void agregarPrestadorDeServicios(PrestadorDeServicios prestadorDeServicios) {

		this.prestadoresDeServicio.add(prestadorDeServicios);
		prestadorDeServicios.setRubro(this);
	}

	public void quitarPrestadorDeServicios(PrestadorDeServicios prestadorDeServicios) {

		this.prestadoresDeServicio.remove(prestadorDeServicios);
		prestadorDeServicios.setRubro(null);
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
	
	public Set<PrestadorDeServicios> getPrestadoresDeServicio() {
		return prestadoresDeServicio;
	}

	/*--------------------------------------------------------------------------------------*/
	/*-------------------------------- Hash code and equals --------------------------------*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getNombre() == null) ? 0 : getNombre().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Rubro))
			return false;
		Rubro other = (Rubro) obj;
		if (getNombre() == null) {
			if (other.getNombre() != null)
				return false;
		} else if (!getNombre().equals(other.getNombre()))
			return false;
		return true;
	}

	@Override
	public int compareTo(Rubro otro) {
		return this.getNombre().compareTo(otro.getNombre());
	}

	/*--------------------------------------------------------------------------------------*/
	
}
