package com.turnolibre.presentation.conversion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;


/**
 * Object mapper de jackson customizado para soportar serialización y deserialización JSON de tipos especificos
 * de hibernate y jodatime.
 *
 * @author Victor Del Rio
 */
public class CustomJacksonObjectMapper extends ObjectMapper {

	public CustomJacksonObjectMapper() {

		registerModule(new Hibernate4Module());
		registerModule(new JodaModule());
	}
}
