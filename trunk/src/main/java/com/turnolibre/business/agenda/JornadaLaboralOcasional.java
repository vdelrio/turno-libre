package com.turnolibre.business.agenda;

import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.i18n.MensajeLocalizable;
import com.turnolibre.business.turno.Horario;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Una jornada laboral ocasional es una jornada que el prestador o profesional habitualmente 
 * no atiende, pero en esta ocasion especial si lo hará. 
 * Ej.: el día de la madre (una fecha especifica, una única vez).
 *
 * @author Victor Del Rio
 */
public class JornadaLaboralOcasional extends JornadaLaboral {

	private Interval intervalo;
	
	
	/*------------------------------------ Constructors ------------------------------------*/

	protected JornadaLaboralOcasional() {
		super();
	}

	public JornadaLaboralOcasional(Interval intervalo, Duration duracionDeLosTurnos, Integer vacantesPorTurno) throws ExcepcionDeReglaDelNegocio {
		super(duracionDeLosTurnos, vacantesPorTurno);

		validarDuracionHorariosNoCoherenteConJornada(duracionDeLosTurnos, intervalo.toDuration());
		validarJornadaPasada(intervalo.getStart());

		this.intervalo = intervalo;
	}

	public JornadaLaboralOcasional(DateTime desde, DateTime hasta, Duration duracionDeLosTurnos, Integer vacantesPorTurno) throws ExcepcionDeReglaDelNegocio {
		this(new Interval(desde, hasta), duracionDeLosTurnos, vacantesPorTurno);
	}

	private JornadaLaboralOcasional(Interval intervalo) {
		super();
		this.intervalo = intervalo;
	}
	
	/*--------------------------------------------------------------------------------------*/
	/*------------------------------------ Public methods ----------------------------------*/

	@Override
	public SortedSet<Horario> getHorarios() {

		if (this.estaLigada())
			return FiltroDeHorarios.ejecutar(this.agenda.getHorarios(), intervalo);
		else
			return new TreeSet<Horario>();
	}

	@Override
	public void actualizarHorarios() {

		DateTime inicioDelHorario = this.intervalo.getStart();

		try {
			do {

				Interval intervaloDelHorario = new Interval(inicioDelHorario, this.duracionDeLosTurnos);
				Horario horario = new Horario(intervaloDelHorario, this.vacantesPorTurno, this.agenda);

				this.agenda.getHorarios().add(horario);
				inicioDelHorario = inicioDelHorario.plus(this.duracionDeLosTurnos);

			} while ( inicioDelHorario.isBefore(this.intervalo.getEnd()) );

		} catch (ExcepcionDeReglaDelNegocio e) {
			// Esta excepcion no es de interez para el cliente del metodo y nunca va a ocurrir
		}
	}

	public void modificarIntervalo(Interval nuevoIntervalo, String motivo) throws ExcepcionDeReglaDelNegocio {

		if (!this.intervalo.equals(nuevoIntervalo)) {

			validarDuracionHorariosNoCoherenteConJornada(duracionDeLosTurnos, nuevoIntervalo.toDuration());

			JornadaLaboralOcasional jornadaAuxiliar = new JornadaLaboralOcasional(nuevoIntervalo);
			validarJornadaSuperpuesta(jornadaAuxiliar, agenda, this);
			this.agenda.borrarHorarios(this.getHorariosNoEnComun(jornadaAuxiliar), motivo);

			// La quitamos y la volvemos a poner para que se regenere en hash code en el set de jornadas de la agenda
			this.agenda.getJornadasLaboralesOcasionales().remove(this);
			this.intervalo = nuevoIntervalo;
			this.agenda.getJornadasLaboralesOcasionales().add(this);

			this.actualizarHorarios();
		}
	}
	
	/*--------------------------------------------------------------------------------------*/
	/*---------------------------------- Protected methods ---------------------------------*/

	@Override
	protected void ligarA(Agenda agenda) throws ExcepcionDeReglaDelNegocio {

		validarjornadaYaLigada();
		validarJornadaSuperpuesta(this, agenda, null);

		this.agenda = agenda;
		this.actualizarHorarios();
	}

	@Override
	protected void desligarDe(Agenda agenda, String motivo) throws ExcepcionDeReglaDelNegocio {

		validarJornadaNoLigada();
		validarJornadaYAgendaNoLigadas(agenda);

		this.borrarHorarios(motivo);
		this.agenda = null;
	}

	@Override
	protected Duration getDuracionDeLosIntervalos() {
		return this.intervalo.toDuration();
	}
	
	/*--------------------------------------------------------------------------------------*/
	/*---------------------------------- Geters and seters ---------------------------------*/

	public Interval getIntervalo() {
		return intervalo;
	}

	/*--------------------------------------------------------------------------------------*/
	/*----------------------------------- Private methods ----------------------------------*/
	
	private boolean seSuperponeConOtrasJornadas(Agenda agenda, JornadaLaboralOcasional jornadaExcluida) {

		SortedSet<Horario> horariosFiltrados = FiltroDeHorarios.ejecutar(agenda.getHorarios(), intervalo);

		if ( jornadaExcluida != null ) {

			SortedSet<Horario> horariosExcluidos = FiltroDeHorarios.ejecutar(agenda.getHorarios(), jornadaExcluida.getIntervalo());
			horariosFiltrados.removeAll(horariosExcluidos);
		}

		return !horariosFiltrados.isEmpty();
	}

	private SortedSet<Horario> getHorariosNoEnComun(JornadaLaboralOcasional otraJornada) {

		SortedSet<Horario> horariosNoEnComun = new TreeSet<Horario>();
		SortedSet<Horario> horariosDeLaOtraJornada = FiltroDeHorarios.ejecutar(agenda.getHorarios(), otraJornada.getIntervalo());

		for (Horario horario : this.getHorarios()) {
			if ( !horariosDeLaOtraJornada.contains(horario) )
				horariosNoEnComun.add(horario);
		}

		return horariosNoEnComun;
	}
	
	/*--------------------------------------------------------------------------------------*/
	/*----------------------------------- Business rules -----------------------------------*/

	private void validarDuracionHorariosNoCoherenteConJornada(Duration duracionDelTurno, Duration duracionDeLosIntervalos) throws ExcepcionDeReglaDelNegocio {

		long minutosQueDuraElTurno = duracionDelTurno.getStandardMinutes();
		long minutosQueDuraLaJornada = duracionDeLosIntervalos.getStandardMinutes();

		if ( minutosQueDuraElTurno > minutosQueDuraLaJornada || minutosQueDuraLaJornada % minutosQueDuraElTurno != 0 )
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.crear.jornada.duracion.horarios.no.coherente.con.jornada"));
	}

	private void validarJornadaPasada(DateTime comienzoDeLaJornada) throws ExcepcionDeReglaDelNegocio {

		if (comienzoDeLaJornada.isBeforeNow())
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.crear.jornada.jornada.pasada"));
	}

	private void validarJornadaSuperpuesta(JornadaLaboralOcasional jornada, Agenda agenda, JornadaLaboralOcasional jornadaExcluida) throws ExcepcionDeReglaDelNegocio {

		if (jornada.seSuperponeConOtrasJornadas(agenda, jornadaExcluida))
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.ligar.jornada.jornada.superpuesta"));
	}

	/*--------------------------------------------------------------------------------------*/
	/*-------------------------------- Hash code and equals --------------------------------*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getAgenda() == null) ? 0 : getAgenda().hashCode());
		result = prime * result + ((getIntervalo() == null) ? 0 : getIntervalo().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof JornadaLaboralOcasional))
			return false;
		JornadaLaboralOcasional other = (JornadaLaboralOcasional) obj;
		if (getAgenda() == null) {
			if (other.getAgenda() != null)
				return false;
		} else if (!getAgenda().equals(other.getAgenda()))
			return false;
		if (getIntervalo() == null) {
			if (other.getIntervalo() != null)
				return false;
		} else if (!getIntervalo().equals(other.getIntervalo()))
			return false;
		return true;
	}

	/*--------------------------------------------------------------------------------------*/
	
}
