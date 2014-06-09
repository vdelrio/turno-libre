package com.turnolibre.presentation.controller;

import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.i18n.MensajeLocalizable;
import com.turnolibre.business.ubicacion.Ciudad;
import com.turnolibre.business.ubicacion.Provincia;
import com.turnolibre.business.usuario.Cliente;
import com.turnolibre.business.usuario.Usuario;
import com.turnolibre.security.UsuarioDeSesion;
import com.turnolibre.service.CiudadService;
import com.turnolibre.service.SharedService;
import com.turnolibre.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;
import java.util.Locale;

@Controller
@SessionAttributes(types = Usuario.class)
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private CiudadService ciudadService;
	@Autowired
	private SharedService sharedService;
	@Autowired
	private MessageSource messageSource;


    /*---------------------------------------- Login ---------------------------------------*/

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

    /*--------------------------------------------------------------------------------------*/
    /*--------------------------------------- Register -------------------------------------*/

	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String initRegistration(Usuario usuario) {
		return "user/register";
	}

	@RequestMapping(value = "register", method = RequestMethod.POST)
	@ResponseBody
	public String register(Usuario usuario, @RequestParam("ciudad") Ciudad ciudad, SessionStatus sessionStatus) throws ExcepcionDeReglaDelNegocio {

		this.usuarioService.registrarUsuario(usuario, ciudad);
		sessionStatus.setComplete();

		return "/";
	}

    /*--------------------------------------------------------------------------------------*/
    /*--------------------------------------- Profile --------------------------------------*/

	@RequestMapping(value = "profile", method = RequestMethod.GET)
	public String initProfile(Model model) {

		Usuario usuario = sharedService.get(Usuario.class, getUsuarioDeSesion().getId());
		Cliente cliente = sharedService.get(Cliente.class, usuario.getRol(Cliente.class).getId());

        Ciudad selectedCity = cliente.getCiudad();

		model.addAttribute("usuario", usuario);
		model.addAttribute("selectedProvincia", selectedCity.getProvincia());
		model.addAttribute("selectedCity", selectedCity);
		model.addAttribute("allCities", ciudadService.findByProvincia(selectedCity.getProvincia().getId()));

		return "user/profile";
	}

	@RequestMapping(value = "update-profile", method = RequestMethod.POST)
	@ResponseBody
	public String updateProfile(Usuario usuario, @RequestParam("ciudad") Ciudad ciudad, SessionStatus sessionStatus, Locale locale) {

		usuario.getRol(Cliente.class).setCiudad(ciudad);

        this.sharedService.update(usuario);
		sessionStatus.setComplete();

		return messageSource.getMessage("edicion.perfil.exitosa", null, locale);
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

    /*--------------------------------------------------------------------------------------*/
    /*-------------------------------------- Ciudades --------------------------------------*/

	// TODO cambiar este metodo porque siempre esta pasando por aca y no es necesario
	@ModelAttribute("allProvincias")
	public List<Provincia> populateCities() {
		return this.sharedService.findAll(Provincia.class);
	}

	@RequestMapping(value = "list-cities", method = RequestMethod.GET)
	@ResponseBody
	public String listCities(Long provinciaId) {
		return this.prepareCitiesOptions(this.ciudadService.findByProvincia(provinciaId));
	}

    /*--------------------------------------------------------------------------------------*/
    /*--------------------------------- Exception Handlers ---------------------------------*/

    @ExceptionHandler(ExcepcionDeReglaDelNegocio.class)
    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
    @ResponseBody
    public String handleExcepcionDeReglaDelNegocio(ExcepcionDeReglaDelNegocio ex, Locale locale) {

        MensajeLocalizable msjLocalizable = ex.getMensaje();
        return messageSource.getMessage(msjLocalizable.getCodigo(), msjLocalizable.getArgumentos(), locale);
    }

    /*--------------------------------------------------------------------------------------*/
    /*----------------------------------- Private methods ----------------------------------*/

    private String prepareCitiesOptions(List<Ciudad> ciudades) {

        StringBuilder sbuilder = new StringBuilder();

        for (Ciudad ciudad : ciudades)
            sbuilder.append("<option value=\"").append(ciudad.getId()).append("\">").append(ciudad.toString()).append("</option>");

        return sbuilder.toString();
    }

    private UsuarioDeSesion getUsuarioDeSesion() {
        return (UsuarioDeSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /*--------------------------------------------------------------------------------------*/

}
