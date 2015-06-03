package com.turnolibre.business.usuario;

import com.turnolibre.business.agenda.Agenda;

import java.util.HashSet;
import java.util.Set;

/**
 * Rol que puede administrar agendas dentro del sistema. Ya sean agendas propias 
 * o de otros profesionales.
 *
 * @author Victor Del Rio
 */
public class AdministradorDeAgenda extends Rol {

	public static String NOMBRE_DE_ROL = "adm-agenda";
	
	private Set<Agenda> agendas = new HashSet<>();
	
	@Override
	public String getNombreDeRol() {
		return NOMBRE_DE_ROL;
	}

	public Set<Agenda> getAgendas() {
		return agendas;
	}

}
