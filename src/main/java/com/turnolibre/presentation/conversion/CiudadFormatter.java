package com.turnolibre.presentation.conversion;

import com.turnolibre.business.ubicacion.Ciudad;
import com.turnolibre.service.SharedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;


public class CiudadFormatter implements Formatter<Ciudad> {

	@Autowired
	private SharedService sharedService;

	@Override
	public Ciudad parse(String text, Locale locale) throws ParseException {
		return this.sharedService.get(Ciudad.class, Long.valueOf(text));
	}

	@Override
	public String print(Ciudad object, Locale locale) {
		return (object != null ? object.getId().toString() : "");
	}

}
