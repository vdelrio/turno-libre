package com.turnolibre.service;

import com.turnolibre.business.ubicacion.Ciudad;

import java.util.List;


public interface CiudadService {

	List<Ciudad> findByProvincia(Long provinciaId);

}
