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

        Query query = getCurrentSession().createQuery("select ps from PrestadorDeServicios ps join ps.servicios s where ps.ubicacion.ciudad = :ciudad and s.id = :servicioId");
        query.setParameter("ciudad", ciudad);
        query.setParameter("servicioId", servicio.getId());

        return query.list();
    }

    public PrestadorDeServicios buscarPorUrl(String url) {

        Query query = getCurrentSession().createQuery("select ps from PrestadorDeServicios ps where ps.url = :url");
        query.setParameter("url", url);

        return (PrestadorDeServicios) query.uniqueResult();
    }


    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

}
