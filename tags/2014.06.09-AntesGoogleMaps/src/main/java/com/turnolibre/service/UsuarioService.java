package com.turnolibre.service;

import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.ubicacion.Ciudad;
import com.turnolibre.business.usuario.Notificacion;
import com.turnolibre.business.usuario.Usuario;

import java.util.SortedSet;


public interface UsuarioService {

	SortedSet<Notificacion> findNotificaciones(Long usuarioId);

	void updateLastLogin(Long usuarioId);
	
	void registrarUsuario(Usuario usuario, Ciudad ciudad) throws ExcepcionDeReglaDelNegocio;

	void changePassword(Long usuarioId, String currentPassword, String newPassword) throws ExcepcionDeReglaDelNegocio;
	
}
