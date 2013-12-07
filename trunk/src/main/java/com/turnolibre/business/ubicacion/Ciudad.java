package com.turnolibre.business.ubicacion;

/**
 * Representa la ciudad en la que se encuentra un prestador de servicios o donde vive un cliente.
 *
 * @author Victor Del Rio
 */
public class Ciudad {

	private Long id;

	private String nombre;
	private String codigoPostal;
	private String provincia;


	/*------------------------------------ Constructors ------------------------------------*/

	public Ciudad() {
		super();
	}

	public Ciudad(String nombre) {
		this.nombre = nombre;
	}

	public Ciudad(String nombre, String codigoPostal, String provincia) {

		this.nombre = nombre;
		this.codigoPostal = codigoPostal;
		this.provincia = provincia;
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

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

    /*--------------------------------------------------------------------------------------*/
    /*-------------------------------- Hash code and equals --------------------------------*/

	@Override
	public boolean equals(Object o) {

		if (this == o) return true;
		if (o == null || !(o instanceof Ciudad)) return false;

		Ciudad otraCiudad = (Ciudad) o;

		if (!this.getNombre().equals(otraCiudad.getNombre())) return false;
		if (this.getCodigoPostal() != null ? !this.getCodigoPostal().equals(otraCiudad.getCodigoPostal()) : otraCiudad.getCodigoPostal() != null) return false;
		if (this.getProvincia() != null ? !this.getProvincia().equals(otraCiudad.getProvincia()) : otraCiudad.getProvincia() != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = this.getNombre().hashCode();
		result = 31 * result + (this.getCodigoPostal() != null ? this.getCodigoPostal().hashCode() : 0);
		result = 31 * result + (this.getProvincia() != null ? this.getProvincia().hashCode() : 0);
		return result;
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

		if (this.getProvincia() != null) {

			sbuilder.append(" - ");
			sbuilder.append(this.getProvincia());
		}

		return sbuilder.toString();
	}

    /*--------------------------------------------------------------------------------------*/

}
