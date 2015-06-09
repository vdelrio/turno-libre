package com.turnolibre.persistence.dao.impl;

import com.turnolibre.business.prestador.PrestadorDeServicios;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ServicioDao {

    @Autowired
    private SessionFactory sessionFactory;


    public List<PrestadorDeServicios> buscarPrestadoresPorCiudad(String nombreServicio, String ciudad) {

        Query query = getCurrentSession().createQuery("from PrestadorDeServicios as ps where ps.ubicacion.ciudad = :ciudad");
        query.setParameter("ciudad", ciudad);

        // TODO armar la query
        return query.list();
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

}
