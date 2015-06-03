package com.turnolibre.business.usuario;

import com.google.common.base.Predicates;
import com.turnolibre.util.CollectionUtils;
import org.joda.time.DateTime;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Usuario del sistema que interactua con el mísmo utilizando un rol determinado.
 *
 * @author Victor Del Rio
 */
public class Usuario {

	private Long id;
	
	private String nombre;
	private String email;
	private String password;
	private String direccion;
	private String telefono;

    private boolean habilitado = true;
	private DateTime ultimoLogueo;

	private Set<Rol> roles = new HashSet<>();
	private SortedSet<Notificacion> notificaciones = new TreeSet<>();


	/*------------------------------------ Constructors ------------------------------------*/

	public Usuario() {
		super();
	}
	
	public Usuario(String nombre, String email, String password) {
		super();
		this.nombre = nombre;
		this.email = email;
		this.password = password;
	}

	public Usuario(String nombre, String email, String password, String direccion) {
		this(nombre, email, password);
		this.direccion = direccion;
	}
	
	public Usuario(String nombre, String email, String password, String direccion, String telefono) {
		this(nombre, email, password, direccion);
		this.telefono = telefono;
	}
	
	/*--------------------------------------------------------------------------------------*/
	/*----------------------------------- Public methods -----------------------------------*/

	public void agregarRol(Rol rol) {

		this.roles.add(rol);
		rol.setUsuario(this);
	}
	
	public void quitarRol(Rol rol) {

		this.roles.remove(rol);
		rol.setUsuario(null);
	}

	public <T extends Rol> T getRol(Class<T> rolClass) {
		return (T) CollectionUtils.get(this.roles, Predicates.instanceOf(rolClass));
	}

	/*--------------------------------------------------------------------------------------*/
	/*---------------------------------- Geters and seters ---------------------------------*/

	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public DateTime getUltimoLogueo() {
		return ultimoLogueo;
	}
	
	public void setUltimoLogueo(DateTime ultimoLogueo) {
		this.ultimoLogueo = ultimoLogueo;
	}

	public Set<Rol> getRoles() {
		return roles;
	}

	public SortedSet<Notificacion> getNotificaciones() {
		return notificaciones;
	}

	/*--------------------------------------------------------------------------------------*/
	/*-------------------------------- Hash code and equals --------------------------------*/
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Usuario))
			return false;
		Usuario other = (Usuario) obj;
		if (getEmail() == null) {
			if (other.getEmail() != null)
				return false;
		} else if (!getEmail().equals(other.getEmail()))
			return false;
		return true;
	}
	
	/*--------------------------------------------------------------------------------------*/
	/*-------------------------------------- Overrides -------------------------------------*/
	
	@Override
	public String toString() {
		return this.email;
	}

	/*--------------------------------------------------------------------------------------*/
	
}
