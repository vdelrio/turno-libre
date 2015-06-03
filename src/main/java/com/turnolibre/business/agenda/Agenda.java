package com.turnolibre.business.agenda;

import com.turnolibre.Configuration;
import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.i18n.MensajeLocalizable;
import com.turnolibre.business.prestador.PrestadorDeServicios;
import com.turnolibre.business.turno.Horario;
import com.turnolibre.business.usuario.AdministradorDeAgenda;
import com.turnolibre.util.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Una agenda es la entidad para la cual se pueden sacar turnos y puede pertenecer a un profesional 
 * y ser administrada por él mísmo o por otras personas. La agenda tiene horarios, cuya hora y 
 * duración son definidos mediante las jornadas laborales.
 *
 * @author Victor Del Rio
 */
public class Agenda {

	private Long id;
	private PrestadorDeServicios prestadorDeServicios;
	
	private String nombre;
	private Period antelacionMaxima;
	private Set<AdministradorDeAgenda> administradores = new HashSet<>();
	
	private SortedSet<Horario> horarios = new TreeSet<>();
	private SortedSet<DiaNoLaboral> diasNoLaborales = new TreeSet<>();
	private SortedSet<JornadaLaboralHabitual> jornadasLaboralesHabituales = new TreeSet<>();
	private Set<JornadaLaboralOcasional> jornadasLaboralesOcasionales = new HashSet<>();
	

	/*------------------------------------ Constructors ------------------------------------*/

	protected Agenda() {
		super();
	}
	
	public Agenda(String nombre, Period antelacionMaxima) {
		this.nombre = nombre;
		this.antelacionMaxima = antelacionMaxima;
	}
	
	/*--------------------------------------------------------------------------------------*/
	/*------------------------------------ Public methods ----------------------------------*/

	public void agregarJornadaLaboralHabitual(JornadaLaboralHabitual jornadaLaboralHabitual) throws ExcepcionDeReglaDelNegocio {

		validarJornadaAsignadaAOtraAgenda(jornadaLaboralHabitual);

		jornadaLaboralHabitual.ligarA(this);
		this.jornadasLaboralesHabituales.add(jornadaLaboralHabitual);
		this.actualizarDiasNoLaborales();
	}

	public void quitarJornadaLaboralHabitual(JornadaLaboralHabitual jornadaLaboralHabitual, String motivo) throws ExcepcionDeReglaDelNegocio {

		validarAgendaNoContieneLaJornadaHabitual(jornadaLaboralHabitual);

		this.jornadasLaboralesHabituales.remove(jornadaLaboralHabitual);
		jornadaLaboralHabitual.desligarDe(this, motivo);
	}

	public void agregarJornadaLaboralOcasional(JornadaLaboralOcasional jornadaLaboralOcasional) throws ExcepcionDeReglaDelNegocio {

		validarJornadaAsignadaAOtraAgenda(jornadaLaboralOcasional);
		validarJornadaMuyLejana(jornadaLaboralOcasional);

		jornadaLaboralOcasional.ligarA(this);
		this.jornadasLaboralesOcasionales.add(jornadaLaboralOcasional);
		this.actualizarDiasNoLaborales();
	}

	public void quitarJornadaLaboralOcasional(JornadaLaboralOcasional jornadaLaboralOcasional, String motivo) throws ExcepcionDeReglaDelNegocio {

		validarAgendaNoContieneLaJornadaOcasional(jornadaLaboralOcasional);

		this.jornadasLaboralesOcasionales.remove(jornadaLaboralOcasional);
		jornadaLaboralOcasional.desligarDe(this, motivo);
	}

	public void agregarDiaNoLaboral(DiaNoLaboral diaNoLaboral) throws ExcepcionDeReglaDelNegocio {

		validarDiaPertenecienteAOtraAgenda(diaNoLaboral);
		validarDiaYaExistente(diaNoLaboral);

		this.diasNoLaborales.add(diaNoLaboral);
		diaNoLaboral.setAgenda(this);

		diaNoLaboral.habilitar();
	}

	public void quitarDiaNoLaboral(DiaNoLaboral diaNoLaboral) throws ExcepcionDeReglaDelNegocio {

		validarAgendaNoContieneAlDia(diaNoLaboral);

		diaNoLaboral.deshabilitar();

		this.diasNoLaborales.remove(diaNoLaboral);
		diaNoLaboral.setAgenda(null);
	}

	public Horario getHorario(Interval intervalo) {

		Horario horario = null;

		try {

			Horario horarioABuscar = new Horario(intervalo, 1, this);
			horario = CollectionUtils.get(this.getHorarios(), horarioABuscar);

		} catch (ExcepcionDeReglaDelNegocio e) {
			// Esta excepcion no es de interez para el cliente del metodo y nunca va a ocurrir
		}

		return horario;
	}

	public void borrarHorarios(SortedSet<Horario> horariosABorrar, String motivo) {

		for (Horario horario : horariosABorrar)
			horario.cancelarTurnos(motivo);

		this.getHorarios().removeAll(horariosABorrar);
	}

	public DateTime getFechaLimiteParaSacarTurno() {
		return new DateTime().plus(this.getAntelacionMaxima()).withTimeAtStartOfDay();
	}
	
	/*--------------------------------------------------------------------------------------*/
	/*---------------------------------- Protected methods ---------------------------------*/

	protected void actualizarDiasNoLaborales() {

		for (DiaNoLaboral diaNoLaboral : this.diasNoLaborales)
			diaNoLaboral.habilitar();
	}

	/*--------------------------------------------------------------------------------------*/
	/*---------------------------------- Geters and seters ---------------------------------*/

	public Long getId() {
		return id;
	}
	
	public PrestadorDeServicios getPrestadorDeServicios() {
		return prestadorDeServicios;
	}
	
	public void setPrestadorDeServicios(PrestadorDeServicios prestadorDeServicios) {
		this.prestadorDeServicios = prestadorDeServicios;
	}
	
	public SortedSet<DiaNoLaboral> getDiasNoLaborales() {
		return diasNoLaborales;
	}
	
	public void setDiasNoLaborales(SortedSet<DiaNoLaboral> diasNoLaborales) {
		this.diasNoLaborales = diasNoLaborales;
	}
	
	public String getNombre() {
		return nombre;
	}

	public SortedSet<Horario> getHorarios() {
		return horarios;
	}

	public Period getAntelacionMaxima() {
		return antelacionMaxima;
	}
	
	public Set<AdministradorDeAgenda> getAdministradores() {
		return administradores;
	}

	public SortedSet<JornadaLaboralHabitual> getJornadasLaboralesHabituales() {
		return jornadasLaboralesHabituales;
	}

	public Set<JornadaLaboralOcasional> getJornadasLaboralesOcasionales() {
		return jornadasLaboralesOcasionales;
	}

	/*--------------------------------------------------------------------------------------*/
	/*----------------------------------- Business rules -----------------------------------*/

	private void validarJornadaAsignadaAOtraAgenda(JornadaLaboral jornadaLaboral) throws ExcepcionDeReglaDelNegocio {

		if (jornadaLaboral.getAgenda() != null)
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.agregar.jornada.jornada.asignada.a.otra.agenda"));
	}

	private void validarAgendaNoContieneLaJornadaHabitual(JornadaLaboralHabitual jornadaLaboralHabitual) throws ExcepcionDeReglaDelNegocio {

		if (!this.jornadasLaboralesHabituales.contains(jornadaLaboralHabitual))
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.quitar.jornada.agenda.no.contiene.la.jornada"));
	}

	private void validarAgendaNoContieneLaJornadaOcasional(JornadaLaboralOcasional jornadaLaboralOcasional) throws ExcepcionDeReglaDelNegocio {

		if (!this.jornadasLaboralesOcasionales.contains(jornadaLaboralOcasional))
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.quitar.jornada.agenda.no.contiene.la.jornada"));
	}

	private void validarJornadaMuyLejana(JornadaLaboralOcasional jornadaOcasional) throws ExcepcionDeReglaDelNegocio {

		Period antelacionMaximaParaCrearHorarios = Configuration.getInstance().getAntelacionMaximaParaCreacionDeHorarios();

		if (jornadaOcasional.getIntervalo().getEnd().isAfter(DateTime.now().plus(antelacionMaximaParaCrearHorarios)))
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.agregar.jornada.ocasional.jornada.muy.lejana"));
	}

	private void validarDiaPertenecienteAOtraAgenda(DiaNoLaboral diaNoLaboral) throws ExcepcionDeReglaDelNegocio {

		if (diaNoLaboral.getAgenda() != null)
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.agregar.dia.no.laboral.dia.perteneciente.a.otra.agenda"));
	}

	private void validarDiaYaExistente(DiaNoLaboral diaNoLaboral) throws ExcepcionDeReglaDelNegocio {

		if (this.diasNoLaborales.contains(diaNoLaboral))
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.agregar.dia.no.laboral.dia.ya.existente"));
	}

	private void validarAgendaNoContieneAlDia(DiaNoLaboral diaNoLaboral) throws ExcepcionDeReglaDelNegocio {

		if (!this.diasNoLaborales.contains(diaNoLaboral))
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.quitar.dia.no.laboral.agenda.no.contiene.al.dia"));
	}

	/*--------------------------------------------------------------------------------------*/
	/*-------------------------------- Hash code and equals --------------------------------*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getNombre() == null) ? 0 : getNombre().hashCode());
		result = prime * result + ((getPrestadorDeServicios() == null) ? 0 : getPrestadorDeServicios().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Agenda))
			return false;
		Agenda other = (Agenda) obj;
		if (getNombre() == null) {
			if (other.getNombre() != null)
				return false;
		} else if (!getNombre().equals(other.getNombre()))
			return false;
		if (getPrestadorDeServicios() == null) {
			if (other.getPrestadorDeServicios() != null)
				return false;
		} else if (!getPrestadorDeServicios().equals(other.getPrestadorDeServicios()))
			return false;
		return true;
	}

	/*--------------------------------------------------------------------------------------*/
	
}
