package com.turnolibre.persistence.dao.impl;

import com.turnolibre.business.ubicacion.Ciudad;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CiudadDao {

	@Autowired
	private SessionFactory sessionFactory;


	public List<Ciudad> findByProvincia(Long provinciaId) {

		Query query = getCurrentSession().createQuery("from Ciudad as ciudad where ciudad.provincia.id = :provinciaId");
		query.setParameter("provinciaId", provinciaId);

		return (List<Ciudad>) query.list();
	}


	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

}
