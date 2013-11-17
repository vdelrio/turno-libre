package com.turnolibre.business.prestador;

import com.turnolibre.business.agenda.Agenda;
import com.turnolibre.business.ubicacion.Ubicacion;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Un prestador de servicios es un establecimiento que ofrece sus servicios 
 * para los cuales hay que sacar turno.
 *
 * @author Victor Del Rio
 */
public class PrestadorDeServicios implements Comparable<PrestadorDeServicios> {

	private Long id;
	private Rubro rubro;
	
	private Set<Servicio> servicios = new TreeSet<Servicio>();
	private Set<Agenda> agendas = new HashSet<Agenda>();
	private SortedSet<Noticia> noticias = new TreeSet<Noticia>();

	private Ubicacion ubicacion;

	private String nombre;
	private String imagen;
	private String direccion;
	private String telefono;
	private String acercaDe;

	
	/*------------------------------------ Constructors ------------------------------------*/

	protected PrestadorDeServicios() {
		super();
	}
	
	public PrestadorDeServicios(String nombre, String direccion, String telefono) {
		super();
		this.nombre = nombre;
		this.direccion = direccion;
		this.telefono = telefono;
	}
	
	public PrestadorDeServicios(String nombre, String direccion, String telefono, String imagen) {
		this(nombre, direccion, telefono);
		this.imagen = imagen;
	}
	
	public PrestadorDeServicios(Rubro rubro, String nombre, String imagen, String direccion, String telefono, String acercaDe) {
		this(nombre, direccion, telefono, imagen);
		this.rubro = rubro;
		this.acercaDe = acercaDe;
	}

	/*--------------------------------------------------------------------------------------*/
	/*------------------------------------ Public methods ----------------------------------*/

	public void agregarServicio(Servicio servicio) {

		this.servicios.add(servicio);
		servicio.setPrestadorDeServicios(this);
	}

	public void quitarServicio(Servicio servicio) {

		this.servicios.remove(servicio);
		servicio.setPrestadorDeServicios(null);
	}

	public void agregarAgenda(Agenda agenda) {

		this.agendas.add(agenda);
		agenda.setPrestadorDeServicios(this);
	}

	public void quitarAgenda(Agenda agenda) {

		this.agendas.remove(agenda);
		agenda.setPrestadorDeServicios(null);
	}
	
	// TODO agregar servicio para agregar dia no laboral a todas las agendas del prestador
	// public void agregarDiaNoLaboral(DiaNoLaboral diaNoLaboral)
	
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
	
	public String getImagen() {
		return imagen;
	}
	
	public void setImagen(String imagen) {
		this.imagen = imagen;
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

	public String getAcercaDe() {
		return acercaDe;
	}

	public void setAcercaDe(String acercaDe) {
		this.acercaDe = acercaDe;
	}

	public Rubro getRubro() {
		return rubro;
	}

	protected void setRubro(Rubro rubro) {
		this.rubro = rubro;
	}

	public Set<Servicio> getServicios() {
		return servicios;
	}

	public Set<Agenda> getAgendas() {
		return agendas;
	}

	public SortedSet<Noticia> getNoticias() {
		return noticias;
	}

	/*--------------------------------------------------------------------------------------*/
	/*-------------------------------- Hash code and equals --------------------------------*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getNombre() == null) ? 0 : getNombre().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PrestadorDeServicios))
			return false;
		PrestadorDeServicios other = (PrestadorDeServicios) obj;
		if (getNombre() == null) {
			if (other.getNombre() != null)
				return false;
		} else if (!getNombre().equals(other.getNombre()))
			return false;
		return true;
	}

	@Override
	public int compareTo(PrestadorDeServicios otro) {
		return this.getNombre().compareTo(otro.getNombre());
	}

	/*--------------------------------------------------------------------------------------*/
	
}
