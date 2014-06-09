package com.turnolibre.business.ubicacion;

/**
 * Representa la ciudad en la que se encuentra un prestador de servicios o donde vive un cliente.
 *
 * @author Victor Del Rio
 */
public class Ciudad {

	private Long id;
	private Provincia provincia;

	private String nombre;
	private String codigoPostal;


	/*------------------------------------ Constructors ------------------------------------*/

	public Ciudad() {
		super();
	}

	public Ciudad(String nombre, Provincia provincia) {

		this.nombre = nombre;
		this.provincia = provincia;
	}

	public Ciudad(String nombre, Provincia provincia, String codigoPostal) {

		this(nombre, provincia);
		this.codigoPostal = codigoPostal;
	}

	/*--------------------------------------------------------------------------------------*/
    /*---------------------------------- Geters and seters ---------------------------------*/

	public Long getId() {
		return id;
	}

	public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

    /*--------------------------------------------------------------------------------------*/
    /*-------------------------------- Hash code and equals --------------------------------*/

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || !(o instanceof Ciudad)) return false;

		Ciudad ciudad = (Ciudad) o;

		if (!id.equals(ciudad.id)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	/*--------------------------------------------------------------------------------------*/
    /*-------------------------------------- Overrides -------------------------------------*/

	@Override
	public String toString() {

		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append(this.getNombre());

		if (this.getCodigoPostal() != null) {

			sbuilder.append(" (");
			sbuilder.append(this.getCodigoPostal());
			sbuilder.append(")");
		}

		return sbuilder.toString();
	}

    /*--------------------------------------------------------------------------------------*/

}
