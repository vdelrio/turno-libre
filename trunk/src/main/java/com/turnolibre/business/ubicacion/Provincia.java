package com.turnolibre.business.ubicacion;

/**
 * Representa a la provincia a la que pertenece una ciudad. Existe solo a modo de segregar a las ciudades.
 *
 * @author Victor Del Rio
 */
public class Provincia {

	private Long id;
	private String nombre;


	/*------------------------------------ Constructors ------------------------------------*/

	public Provincia() {
		super();
	}

	public Provincia(String nombre) {
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

    /*--------------------------------------------------------------------------------------*/
    /*-------------------------------- Hash code and equals --------------------------------*/

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || !(o instanceof Provincia)) return false;

		Provincia provincia = (Provincia) o;

		if (!nombre.equals(provincia.nombre)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return nombre.hashCode();
	}

	/*--------------------------------------------------------------------------------------*/
    /*-------------------------------------- Overrides -------------------------------------*/

	@Override
	public String toString() {
		return this.nombre;
	}

    /*--------------------------------------------------------------------------------------*/

}
