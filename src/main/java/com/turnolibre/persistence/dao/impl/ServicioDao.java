package com.turnolibre.persistence.dao.impl;

import com.turnolibre.business.prestador.Servicio;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServicioDao {

    @Autowired
    private SessionFactory sessionFactory;


    public Servicio buscarPorNombre(String nombre) {

        Query query = getCurrentSession().createQuery("select s from Servicio s where s.nombre = :nombre");
        query.setParameter("nombre", nombre);

        return (Servicio) query.uniqueResult();
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

}
