package com.turnolibre.security;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UsuarioDeSesion extends User {

	private Long id;
	private String nombre;

	public UsuarioDeSesion(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id, String nombre) {
		super(username, password, authorities);
		this.id = id;
		this.nombre = nombre;
	}

	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

}
