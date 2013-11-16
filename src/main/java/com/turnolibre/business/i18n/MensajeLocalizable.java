package com.turnolibre.business.i18n;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Representa a un mensaje que podrá ser visualizado en diferentes idiomas.
 *
 * @author Victor Del Rio
 */
public class MensajeLocalizable {

	private String codigo;
	private String argumentos;


	/*------------------------------------ Constructors ------------------------------------*/

	protected MensajeLocalizable() {
		super();
	}

	public MensajeLocalizable(String codigo) {
		this.codigo = codigo;
	}

	public MensajeLocalizable(String codigo, List<String> argumentos) {
		this(codigo);
		this.argumentos = Joiner.on(",").join(argumentos);
	}

    /*--------------------------------------------------------------------------------------*/
    /*---------------------------------- Geters and seters ---------------------------------*/

	public String getCodigo() {
		return codigo;
	}

	public List<String> getArgumentos() {
		return Lists.newArrayList(Splitter.on(",").split(argumentos));
	}

    /*--------------------------------------------------------------------------------------*/


}
