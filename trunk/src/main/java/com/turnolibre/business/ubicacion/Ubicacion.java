package com.turnolibre.business.ubicacion;

/**
 * Representa a la ubicación de un prestador de servicios o de un usuario.
 *
 * @author Victor Del Rio
 */
public class Ubicacion {

	private String ciudad;
	private String direccion;
	private double latitud;
	private double longitud;


	public Ubicacion() {
	}

	public Ubicacion(String ciudad, String direccion, double latitud, double longitud) {
		this.ciudad = ciudad;
		this.direccion = direccion;
		this.latitud = latitud;
		this.longitud = longitud;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
}
