package com.turnolibre.business.excepcion;

import com.turnolibre.business.i18n.MensajeLocalizable;

/**
 * Excepcion que ocurre cuando se ha roto un regla del negocio.
 *
 * @author Victor Del Rio
 */
public class ExcepcionDeReglaDelNegocio extends Exception {

	private MensajeLocalizable mensaje;


	public ExcepcionDeReglaDelNegocio(MensajeLocalizable mensaje) {
		this.mensaje = mensaje;
	}

	public MensajeLocalizable getMensaje() {
		return mensaje;
	}

}
