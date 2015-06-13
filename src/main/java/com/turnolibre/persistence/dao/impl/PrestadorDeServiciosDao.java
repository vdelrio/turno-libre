package com.turnolibre.persistence.dao.impl;

import com.turnolibre.business.prestador.PrestadorDeServicios;
import com.turnolibre.business.prestador.Servicio;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PrestadorDeServiciosDao {

    @Autowired
    private SessionFactory sessionFactory;


    public List<PrestadorDeServicios> buscarPorServicioYCiudad(Servicio servicio, String ciudad) {

        Query query = getCurrentSession().createQuery("from PrestadorDeServicios as ps where ps.ubicacion.ciudad = :ciudad and :servicio in ps.servicios");
        query.setParameter("ciudad", ciudad);
        query.setParameter("servicio", servicio);

        return query.list();

    }


    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

}
