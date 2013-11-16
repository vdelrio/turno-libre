package com.turnolibre.service;

import com.turnolibre.business.turno.Turno;

import java.util.SortedSet;


public interface ClienteService {

	SortedSet<Turno> findTurnos(Long clienteId);

}
