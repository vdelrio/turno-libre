package com.turnolibre.business.usuario;

import com.turnolibre.business.turno.Turno;
import com.turnolibre.business.ubicacion.Ciudad;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Rol que accede al sistema con la intención de sacar turnos en los prestadores de servicios.
 *
 * @author Victor Del Rio
 */
public class Cliente extends Rol {

	public static String NOMBRE_DE_ROL = "cliente";
	
	private Ciudad ciudad;
	private SortedSet<Turno> turnos = new TreeSet<Turno>();


	/*------------------------------------ Constructors ------------------------------------*/

	public Cliente() {
		super();
	}

	public Cliente(Ciudad ciudad) {
		super();
		this.ciudad = ciudad;
	}

    /*--------------------------------------------------------------------------------------*/
	/*------------------------------------ Public methods ----------------------------------*/

	@Override
	public String getNombreDeRol() {
		return NOMBRE_DE_ROL;
	}

    /*--------------------------------------------------------------------------------------*/
	/*---------------------------------- Geters and seters ---------------------------------*/

	public Ciudad getCiudad() {
		return ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	public SortedSet<Turno> getTurnos() {
		return turnos;
	}

	/*--------------------------------------------------------------------------------------*/

}
