package com.turnolibre;


import org.joda.time.Duration;
import org.joda.time.Period;

import java.io.IOException;
import java.util.Properties;

public class Configuration {

	private static final String CONFIG_PROPERTIES_PATH = "business-defaults.properties";

	private static Configuration instance = new Configuration();
	private Properties configurationFile = null;


    /*------------------------------------ Constructors ------------------------------------*/

	private Configuration() {

		configurationFile = new Properties();

		try {
			configurationFile.load(getClass().getClassLoader().getResourceAsStream(CONFIG_PROPERTIES_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    /*--------------------------------------------------------------------------------------*/
	/*------------------------------------ Static methods ----------------------------------*/

	public static Configuration getInstance() {
		return instance;
	}

    /*--------------------------------------------------------------------------------------*/
	/*------------------------------------ Public methods ----------------------------------*/

	public Period getAntelacionMaximaParaCreacionDeHorarios() {
		return Period.parse(configurationFile.getProperty("agenda.antelacion.maxima.creacion.horarios"));
	}

	public Duration getDuracionMinimaDeHorarios() {
		return Duration.parse(configurationFile.getProperty("horario.duracion.minima"));
	}

	public Duration getDuracionDefectoDeHorarios() {
		return Duration.parse(configurationFile.getProperty("horario.duracion.defecto"));
	}

	/*--------------------------------------------------------------------------------------*/

}
