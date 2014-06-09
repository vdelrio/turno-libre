package com.turnolibre.business.ubicacion;

/**
 *  Representa al barrio en el que se encuentra un prestador de servicios.
 *
 * @author Victor Del Rio
 */
public class Barrio {

	private Long id;
	private Ciudad ciudad;

	private String nombre;


	/*------------------------------------ Constructors ------------------------------------*/

	protected Barrio() {
		super();
	}

	public Barrio(String nombre, Ciudad ciudad) {

		this.nombre = nombre;
		this.ciudad = ciudad;
	}

	/*--------------------------------------------------------------------------------------*/
    /*---------------------------------- Geters and seters ---------------------------------*/

	public Long getId() {
		return id;
	}

	public Ciudad getCiudad() {
		return ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/*--------------------------------------------------------------------------------------*/
    /*-------------------------------- Hash code and equals --------------------------------*/

	@Override
	public boolean equals(Object o) {

		if (this == o) return true;
		if (o == null || !(o instanceof Barrio)) return false;

		Barrio otroBarrio = (Barrio) o;

		if (!this.getCiudad().equals(otroBarrio.getCiudad())) return false;
		if (!this.getNombre().equals(otroBarrio.getNombre())) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = ciudad.hashCode();
		result = 31 * result + nombre.hashCode();
		return result;
	}

	/*--------------------------------------------------------------------------------------*/
	/*-------------------------------------- Overrides -------------------------------------*/

	@Override
	public String toString() {
		return this.getNombre();
	}

    /*--------------------------------------------------------------------------------------*/

}
