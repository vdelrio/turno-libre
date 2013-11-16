package com.turnolibre.presentation.controller;

import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.i18n.MensajeLocalizable;
import com.turnolibre.business.usuario.Usuario;
import com.turnolibre.security.UsuarioDeSesion;
import com.turnolibre.service.SharedService;
import com.turnolibre.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Locale;

@Controller
@SessionAttributes(types = Usuario.class)
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private SharedService sharedService;
	@Autowired
	private MessageSource messageSource;


	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String initLogin(Model model) {

		model.addAttribute("usuario", new Usuario());
		return "user/login";
	}

	@RequestMapping(value = "login/error", method = RequestMethod.GET)
	public String initLoginWithError(Model model) {

		model.addAttribute("loginError", true);
		return "user/login";
	}

	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String initRegistration(Usuario usuario) {
		return "user/register";
	}

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String register(Usuario usuario, BindingResult bindingResult, SessionStatus sessionStatus) {

		if (bindingResult.hasErrors()) {
			return "user/register";
		}

		try {

			this.usuarioService.registrarUsuario(usuario);
			sessionStatus.setComplete();

		} catch (ExcepcionDeReglaDelNegocio e) {

			// TODO agregar un error que sepa mostrar la vista
			return "user/register";
		}

		return "redirect:/";
	}

	@RequestMapping(value = "profile", method = RequestMethod.GET)
	public String initProfile(Model model) {

		model.addAttribute("usuario", sharedService.get(Usuario.class, getUsuarioDeSesion().getId()));
		return "user/profile";
	}

	@RequestMapping(value = "update-profile", method = RequestMethod.POST)
	public String updateProfile(Usuario usuario, BindingResult bindingResult, SessionStatus sessionStatus) {

		if (bindingResult.hasErrors()) {
			return "user/profile";
		}

		this.sharedService.update(usuario);
		sessionStatus.setComplete();

		return "redirect:/user/profile";
	}

	@RequestMapping(value = "change-password", method = RequestMethod.POST)
	@ResponseBody
	public String changePassword(@RequestParam("contrasena-actual") String currentPassword,
						 @RequestParam("nueva-contrasena") String newPassword,
						 @RequestParam("verificacion-contrasena") String passwordCheck, Locale locale) throws ExcepcionDeReglaDelNegocio {

		if (!passwordCheck.equals(newPassword)) {
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("diferentes.passwords"));
		}

		usuarioService.changePassword(getUsuarioDeSesion().getId(), currentPassword, newPassword);

		return messageSource.getMessage("modificacion.contraseña.exitosa", null, locale);
	}

	@ExceptionHandler(ExcepcionDeReglaDelNegocio.class)
	@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
	@ResponseBody
	public String handleExcepcionDeReglaDelNegocio(ExcepcionDeReglaDelNegocio ex, Locale locale) {

		MensajeLocalizable msjLocalizable = ex.getMensaje();
		return messageSource.getMessage(msjLocalizable.getCodigo(), null, locale);

		// TODO hacer que sirva con o sin args
//		return messageSource.getMessage(msjLocalizable.getCodigo(), msjLocalizable.getArgumentos().toArray(), locale);
	}



	private UsuarioDeSesion getUsuarioDeSesion() {
		return (UsuarioDeSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
