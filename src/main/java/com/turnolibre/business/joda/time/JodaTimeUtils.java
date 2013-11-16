package com.turnolibre.business.joda.time;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.Period;

/**
 * Clase utilitaria para el manejo temporal complejo.
 *
 * @author Victor Del Rio
 */
public class JodaTimeUtils {

	private static JodaTimeUtils instance = new JodaTimeUtils();

	/*-------------------------------------- Singleton -------------------------------------*/

	private JodaTimeUtils() {
		// Private singleton constructor
	}

	public static JodaTimeUtils getInstance() {
		return instance;
	}

	/*--------------------------------------------------------------------------------------*/
	/*------------------------------------ Public methods ----------------------------------*/

	public Interval moveInterval(Interval intervalo, Period periodo) {
		return new Interval(intervalo.getStart().plus(periodo), intervalo.getEnd().plus(periodo));
	}
	
	public DateTime getDateTimeEnSemanaActual(DayOfWeekTime dayOfWeekTime) {
		
		DateTime dateTime = new DateTime().
				withDayOfWeek(dayOfWeekTime.getDayOfWeek()).
				withTime(dayOfWeekTime.getHourOfDay(), dayOfWeekTime.getMinuteOfHour(), 0, 0);
		
		if ( dateTime.isBeforeNow() ) {
			dateTime = dateTime.plusWeeks(1);
		}
		
		return dateTime;
	}

	public Interval getIntervaloEnSemanaActual(DayOfWeekTime diaYHoraDeInicio, DayOfWeekTime diaYHoraDeFin) {
		
		DateTime fechaDelDiaDeLaSemanaDeInicio = getDateTimeEnSemanaActual(diaYHoraDeInicio);
		DateTime fechaDelDiaDeLaSemanaDeFin = getDateTimeEnSemanaActual(diaYHoraDeFin);
		
		if (fechaDelDiaDeLaSemanaDeFin.isBefore(fechaDelDiaDeLaSemanaDeInicio))
			fechaDelDiaDeLaSemanaDeFin = fechaDelDiaDeLaSemanaDeFin.plusWeeks(1);
		
		return new Interval(fechaDelDiaDeLaSemanaDeInicio, fechaDelDiaDeLaSemanaDeFin);
	}
	
	public Duration toDuration(DayOfWeekTime diaYHoraDeInicio, DayOfWeekTime diaYHoraDeFin) {
		return getIntervaloEnSemanaActual(diaYHoraDeInicio, diaYHoraDeFin).toDuration();
	}

	/*--------------------------------------------------------------------------------------*/

}
