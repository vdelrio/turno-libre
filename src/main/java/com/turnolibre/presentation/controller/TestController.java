package com.turnolibre.presentation.controller;

import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.i18n.MensajeLocalizable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Locale;

@Controller
@RequestMapping("/test")
public class TestController {

	@Autowired
	private MessageSource messageSource;


	@RequestMapping
	public String initPage(Model model) {
		return "test";
	}

	@RequestMapping(value = "ajax", method = RequestMethod.POST)
	public void ajaxAction() throws ExcepcionDeReglaDelNegocio {

		if (true) {
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("notificacion.habilitacion.por.prestador", Arrays.asList("lunes 10 de octubre", "Futbol 5 mentarios", "Cancha 2")));
		}


	}

	@ExceptionHandler(ExcepcionDeReglaDelNegocio.class)
	@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
	@ResponseBody
	public String handleExcepcionDeReglaDelNegocio(ExcepcionDeReglaDelNegocio ex, Locale locale) {

		MensajeLocalizable msjLocalizable = ex.getMensaje();
		return messageSource.getMessage(msjLocalizable.getCodigo(), msjLocalizable.getArgumentos().toArray(), locale);
	}

}
