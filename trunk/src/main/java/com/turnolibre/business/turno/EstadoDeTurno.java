package com.turnolibre.business.turno;

/**
 * Estados en los que pueden estar los turnos y los horarios, dependiendo de sus turnos.
 *
 * @author Victor Del Rio
 */
public enum EstadoDeTurno {
	
	/**
	 * El turno no tiene cliente asignado / el horario tiene al menos un turno libre.
	 */
	LIBRE,
	/**
	 * El turno ya tiene un cliente asignado / el horario tiene todos los turnos ocupados 
	 * o algunos ocupados y los demás deshabilitados.
	 */
	OCUPADO,
	/**
	 * El turno tenga o no cliente asigando no se realizará porque el profesional 
	 * no puede asistir. De ser rehabilitado, el turno pasara a "libre" si no tenía cliente 
	 * asignado o a "ocupado" si es que tenía cliente asignado. Un horario estará 
	 * deshabilitado si todos sus turnos lo están.
	 */
	DESHABILITADO, 
	/** 
	 * No se ofrecen servicios para un determinado horario.
	 */
	INEXISTENTE
	
}
