package com.turnolibre.business.ubicacion;

/**
 * Representa a la ubicación de un prestador de servicios.
 *
 * @author Victor Del Rio
 */
public class Ubicacion {

	private Ciudad ciudad;
	private String direccion;

	private Barrio barrio;


	/*------------------------------------ Constructors ------------------------------------*/

	protected Ubicacion() {
		super();
	}

	public Ubicacion(Ciudad ciudad, String direccion) {

		this.ciudad = ciudad;
		this.direccion = direccion;
	}

	public Ubicacion(Ciudad ciudad, Barrio barrio, String direccion) {

		this.ciudad = ciudad;
		this.barrio = barrio;
		this.direccion = direccion;
	}

	/*--------------------------------------------------------------------------------------*/
    /*---------------------------------- Geters and seters ---------------------------------*/

	public Ciudad getCiudad() {
		return ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Barrio getBarrio() {
		return barrio;
	}

	public void setBarrio(Barrio barrio) {
		this.barrio = barrio;
	}

	/*--------------------------------------------------------------------------------------*/
    /*-------------------------------- Hash code and equals --------------------------------*/

	@Override
	public boolean equals(Object o) {

		if (this == o) return true;
		if (o == null || !(o instanceof Ubicacion)) return false;

		Ubicacion otraUbicacion = (Ubicacion) o;

		if (!this.getCiudad().equals(otraUbicacion.getCiudad())) return false;
		if (!this.getDireccion().equals(otraUbicacion.getDireccion())) return false;
		if (this.getBarrio() != null ? !this.getBarrio().equals(otraUbicacion.getBarrio()) : otraUbicacion.getBarrio() != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = this.getCiudad().hashCode();
		result = 31 * result + this.getDireccion().hashCode();
		result = 31 * result + (this.getBarrio() != null ? this.getBarrio().hashCode() : 0);
		return result;
	}

	/*--------------------------------------------------------------------------------------*/

}
