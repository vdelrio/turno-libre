package com.turnolibre.service;

import com.turnolibre.business.prestador.PrestadorDeServicios;

import java.util.Set;


public interface RubroService {

	Set<PrestadorDeServicios> findPrestadores(Long rubroId);
	
}
