package com.turnolibre.business.usuario;

import com.turnolibre.business.turno.Turno;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Rol que accede al sistema con la intención de sacar turnos en los prestadores de servicios.
 *
 * @author Victor Del Rio
 */
public class Cliente extends Rol {

	public static String NOMBRE_DE_ROL = "cliente";
	
	private SortedSet<Turno> turnos = new TreeSet<>();


	/*------------------------------------ Public methods ----------------------------------*/

	@Override
	public String getNombreDeRol() {
		return NOMBRE_DE_ROL;
	}

    /*--------------------------------------------------------------------------------------*/
	/*---------------------------------- Geters and seters ---------------------------------*/

	public SortedSet<Turno> getTurnos() {
		return turnos;
	}

	/*--------------------------------------------------------------------------------------*/

}
