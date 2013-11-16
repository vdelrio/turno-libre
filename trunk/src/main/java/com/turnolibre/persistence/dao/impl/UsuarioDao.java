package com.turnolibre.persistence.dao.impl;

import com.turnolibre.business.usuario.Usuario;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDao {

	@Autowired
	private SessionFactory sessionFactory;


	public Usuario findByUsername(String username) {

		Query query = getCurrentSession().createQuery("from Usuario as usuario where usuario.email = :email");
		query.setParameter("email", username);

		return (Usuario) query.uniqueResult();
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

}
