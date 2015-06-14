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

		Query query = getCurrentSession().createQuery("select u from Usuario u where u.email = :email");
		query.setParameter("email", username);

		return (Usuario) query.uniqueResult();
	}

	public boolean emailExists(String email) {
		return this.findByUsername(email) != null;
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

}
