package com.turnolibre.business.usuario;

/**
 * Clase base para los roles que puede tomar un usuario del sistema.
 *
 * @author Victor Del Rio
 */
public abstract class Rol {

	private Long id;
	private Usuario usuario;

	
	/*------------------------------------ Constructors ------------------------------------*/

	protected Rol() {
        super();
    }
	
	/*--------------------------------------------------------------------------------------*/
	/*----------------------------------- Abstract methods ---------------------------------*/

    public abstract String getNombreDeRol();

	/*--------------------------------------------------------------------------------------*/
	/*---------------------------------- Geters and seters ---------------------------------*/

	public Long getId() {
		return id;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	
	protected void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	/*--------------------------------------------------------------------------------------*/
	/*-------------------------------- Hash code and equals --------------------------------*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getNombreDeRol() == null) ? 0 : getNombreDeRol().hashCode());
		result = prime * result + ((getUsuario() == null) ? 0 : getUsuario().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Rol))
			return false;
		Rol other = (Rol) obj;
		if (getNombreDeRol() == null) {
			if (other.getNombreDeRol() != null)
				return false;
		} else if (!getNombreDeRol().equals(other.getNombreDeRol()))
			return false;
		if (getUsuario() == null) {
			if (other.getUsuario() != null)
				return false;
		} else if (!getUsuario().equals(other.getUsuario()))
			return false;
		return true;
	}

	/*--------------------------------------------------------------------------------------*/
	
}
