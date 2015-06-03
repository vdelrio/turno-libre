package com.turnolibre.business.agenda;

import com.turnolibre.Configuration;
import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.i18n.MensajeLocalizable;
import com.turnolibre.business.joda.time.DayOfWeekTime;
import com.turnolibre.business.joda.time.JodaTimeUtils;
import com.turnolibre.business.turno.Horario;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.Period;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Las jornadas laborales habituales son las jornadas laborales que el prestador de servicios 
 * o profesional atiende todas las semanas de igual manera. Ej.: lunes de 9 a 18hs.
 *
 * @author Victor Del Rio
 */
public class JornadaLaboralHabitual extends JornadaLaboral implements Comparable<JornadaLaboralHabitual> {

	private DayOfWeekTime diaYHoraDeInicio;
	private DayOfWeekTime diaYHoraDeFin;
	
	
	/*------------------------------------ Constructors ------------------------------------*/

	protected JornadaLaboralHabitual() {
		super();
	}

	public JornadaLaboralHabitual(DayOfWeekTime diaYHoraDeInicio, DayOfWeekTime diaYHoraDeFin, Duration duracionDeLosTurnos, Integer vacantesPorTurno) throws ExcepcionDeReglaDelNegocio {
		super(duracionDeLosTurnos, vacantesPorTurno);

		validarDuracionHorariosNoCoherenteConJornada(diaYHoraDeInicio, diaYHoraDeFin, duracionDeLosTurnos);

		this.diaYHoraDeInicio = diaYHoraDeInicio;
		this.diaYHoraDeFin = diaYHoraDeFin;
	}

	private JornadaLaboralHabitual(DayOfWeekTime diaYHoraDeInicio, DayOfWeekTime diaYHoraDeFin) {
		super();
		this.diaYHoraDeInicio = diaYHoraDeInicio;
		this.diaYHoraDeFin = diaYHoraDeFin;
	}
	
	/*--------------------------------------------------------------------------------------*/
	/*------------------------------------ Public methods ----------------------------------*/

	@Override
	public SortedSet<Horario> getHorarios() {

		if (this.estaLigada())
			return FiltroDeHorarios.ejecutar(this.agenda.getHorarios(), this.getIntervalos());
		else
			return new TreeSet<>();
	}

	public void modificarIntervalos(DayOfWeekTime nuevoDiaYHoraDeInicio, DayOfWeekTime nuevoDiaYHoraDeFin, String motivo) throws ExcepcionDeReglaDelNegocio {

		if (!this.diaYHoraDeInicio.equals(nuevoDiaYHoraDeInicio) || !this.diaYHoraDeFin.equals(nuevoDiaYHoraDeFin)) {

			validarDuracionHorariosNoCoherenteConJornada(nuevoDiaYHoraDeInicio, nuevoDiaYHoraDeFin, this.duracionDeLosTurnos);

			JornadaLaboralHabitual jornadaAuxiliar = new JornadaLaboralHabitual(nuevoDiaYHoraDeInicio, nuevoDiaYHoraDeFin);
			validarJornadaSuperpuesta(jornadaAuxiliar, agenda, this);
			this.agenda.borrarHorarios(this.getHorariosNoEnComun(jornadaAuxiliar), motivo);

			// La quitamos y la volvemos a poner para que se regenere en hash code en el set de jornadas de la agenda
			this.agenda.getJornadasLaboralesHabituales().remove(this);
			this.diaYHoraDeInicio = nuevoDiaYHoraDeInicio;
			this.diaYHoraDeFin = nuevoDiaYHoraDeFin;
			this.agenda.getJornadasLaboralesHabituales().add(this);

			this.actualizarHorarios();
		}
	}

	// Por cada intervalo de la jornada, crea los horarios si es que éstos no existen todavía
	@Override
	public void actualizarHorarios() throws ExcepcionDeReglaDelNegocio {

		for (Interval intervalo : this.getIntervalos())
			this.crearHorarios(intervalo);

		this.agenda.actualizarDiasNoLaborales();
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
		return JodaTimeUtils.getInstance().toDuration(this.diaYHoraDeInicio, this.diaYHoraDeFin);
	}
	
	/*--------------------------------------------------------------------------------------*/
	/*---------------------------------- Geters and seters ---------------------------------*/

	public DayOfWeekTime getDiaYHoraDeInicio() {
		return diaYHoraDeInicio;
	}

	public DayOfWeekTime getDiaYHoraDeFin() {
		return diaYHoraDeFin;
	}

	/*--------------------------------------------------------------------------------------*/
	/*----------------------------------- Private methods ----------------------------------*/

	private Set<Interval> getIntervalos() {
		return this.generarIntervalos(this.agenda.getAntelacionMaxima());
	}

	private boolean seSuperponeConOtrasJornadas(Agenda agenda, JornadaLaboralHabitual jornadaExcluida) {

		Set<Interval> intervalosPropios = this.generarIntervalos(Configuration.getInstance().getAntelacionMaximaParaCreacionDeHorarios());
		SortedSet<Horario> horariosFiltrados = FiltroDeHorarios.ejecutar(agenda.getHorarios(), intervalosPropios);

		if ( jornadaExcluida != null ) {

			Set<Interval> intervalosExcluidos = jornadaExcluida.generarIntervalos(Configuration.getInstance().getAntelacionMaximaParaCreacionDeHorarios());
			SortedSet<Horario> horariosExcluidos = FiltroDeHorarios.ejecutar(agenda.getHorarios(), intervalosExcluidos);
			horariosFiltrados.removeAll(horariosExcluidos);
		}

		return !horariosFiltrados.isEmpty();
	}

	private SortedSet<Horario> getHorariosNoEnComun(JornadaLaboralHabitual otraJornada) {

		SortedSet<Horario> horariosNoEnComun = new TreeSet<>();

		Set<Interval> intervalosDeLaOtraJornada = otraJornada.generarIntervalos(this.agenda.getAntelacionMaxima());
		SortedSet<Horario> horariosDeLaOtraJornada = FiltroDeHorarios.ejecutar(agenda.getHorarios(), intervalosDeLaOtraJornada);

		for (Horario horario : this.getHorarios()) {
			if ( !horariosDeLaOtraJornada.contains(horario) )
				horariosNoEnComun.add(horario);
		}

		return horariosNoEnComun;
	}

	// Genera los intervalos para el periodo comenzando desde la fecha actual
	private Set<Interval> generarIntervalos(Period periodo) {

		Set<Interval> intervalos = new HashSet<>();
		Interval intervalo = JodaTimeUtils.getInstance().getIntervaloEnSemanaActual(this.diaYHoraDeInicio, this.diaYHoraDeFin);

		do {
			intervalos.add(intervalo);
			intervalo = JodaTimeUtils.getInstance().moveInterval(intervalo, Period.weeks(1));

		} while ( intervalo.getEnd().isBefore(DateTime.now().plus(periodo)) );

		return intervalos;
	}

	// Crea los horarios en la agenda para un intervalo, si un horario ya existia no lo pisa
	private void crearHorarios(Interval intervalo) throws ExcepcionDeReglaDelNegocio {

		DateTime inicioDelHorario = intervalo.getStart();

		do {

			Interval intervaloDelHorario = new Interval(inicioDelHorario, this.duracionDeLosTurnos);
			Horario horario = new Horario(intervaloDelHorario, this.vacantesPorTurno, this.agenda);

			this.agenda.getHorarios().add(horario);
			inicioDelHorario = inicioDelHorario.plus(this.duracionDeLosTurnos);

		} while ( inicioDelHorario.isBefore(intervalo.getEnd()) );

	}
	
	/*--------------------------------------------------------------------------------------*/
	/*----------------------------------- Business rules -----------------------------------*/

	private void validarDuracionHorariosNoCoherenteConJornada(DayOfWeekTime diaYHoraDeInicio, DayOfWeekTime diaYHoraDeFin,
															  Duration duracionDeLosTurnos) throws ExcepcionDeReglaDelNegocio {

		long minutosQueDuraElTurno = duracionDeLosTurnos.getStandardMinutes();
		long minutosQueDuraLaJornada = JodaTimeUtils.getInstance().toDuration(diaYHoraDeInicio, diaYHoraDeFin).getStandardMinutes();

		if (minutosQueDuraElTurno > minutosQueDuraLaJornada || minutosQueDuraLaJornada % minutosQueDuraElTurno != 0)
			throw new ExcepcionDeReglaDelNegocio(new MensajeLocalizable("excepcion.crear.jornada.duracion.horarios.no.coherente.con.jornada"));
	}

	private void validarJornadaSuperpuesta(JornadaLaboralHabitual jornada, Agenda agenda, JornadaLaboralHabitual jornadaExcluida) throws ExcepcionDeReglaDelNegocio {

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
		result = prime * result + ((getDiaYHoraDeFin() == null) ? 0 : getDiaYHoraDeFin().hashCode());
		result = prime * result + ((getDiaYHoraDeInicio() == null) ? 0 : getDiaYHoraDeInicio().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof JornadaLaboralHabitual))
			return false;
		JornadaLaboralHabitual other = (JornadaLaboralHabitual) obj;
		if (getAgenda() == null) {
			if (other.getAgenda() != null)
				return false;
		} else if (!getAgenda().equals(other.getAgenda()))
			return false;
		if (getDiaYHoraDeFin() == null) {
			if (other.getDiaYHoraDeFin() != null)
				return false;
		} else if (!getDiaYHoraDeFin().equals(other.getDiaYHoraDeFin()))
			return false;
		if (getDiaYHoraDeInicio() == null) {
			if (other.getDiaYHoraDeInicio() != null)
				return false;
		} else if (!getDiaYHoraDeInicio().equals(other.getDiaYHoraDeInicio()))
			return false;
		return true;
	}

	@Override
	public int compareTo(JornadaLaboralHabitual otra) {
		return getDiaYHoraDeInicio().compareTo(otra.getDiaYHoraDeInicio());
	}

	/*--------------------------------------------------------------------------------------*/
	
}
