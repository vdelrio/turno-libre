package com.turnolibre.business.usuario;

/**
 * Rol que puede administrar lo correpondiente a un prestador de servicios en particular.
 *
 * @author Victor Del Rio
 */
public class AdministradorDePrestador extends Rol {

    public static String NOMBRE_DE_ROL = "adm-prestador";

    @Override
    public String getNombreDeRol() {
        return NOMBRE_DE_ROL;
    }

}
