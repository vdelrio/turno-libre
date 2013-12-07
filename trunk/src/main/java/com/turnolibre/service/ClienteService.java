package com.turnolibre.service;

import com.turnolibre.business.turno.Turno;
import com.turnolibre.business.usuario.Cliente;

import java.util.SortedSet;


public interface ClienteService {

	Cliente findByUsuario(Long usuarioId);

	SortedSet<Turno> findTurnos(Long clienteId);

}
