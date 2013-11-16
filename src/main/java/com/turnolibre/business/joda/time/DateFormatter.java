package com.turnolibre.business.joda.time;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * Clase utilitaria para el formato de fechas de la librería joda time.
 *
 * @author Victor Del Rio
 */
// TODO i18n
public class DateFormatter {

	private static PeriodFormatter dayHourMinutesFormatter;


	/*------------------------------------ Public methods ----------------------------------*/

	public static String formatWeek(LocalDate monday, LocalDate sunday) {
		
		if (sunday.getMonthOfYear() > monday.getMonthOfYear())
			return monday.toString("d 'de' MMMM") + " - " + sunday.toString("d 'de' MMMM 'de' YYYY");
		else
			return monday.toString("d") + " - " + sunday.toString("d 'de' MMMM 'de' YYYY");
	}
	
	public static String formatWeekDay(LocalDate date) {
		return date.toString("E d/M");
	}
	
	public static String formatInterval(Interval interval) {
		
		DateTime start = interval.getStart();
		DateTime end = interval.getEnd();
		
		if (start.getDayOfYear() == end.getDayOfYear()) 
			return start.toString("EEEE d 'de' MMMM 'de' HH:mm'hs a' ") + end.toString("HH:mm'hs'");
		else
			return start.toString("'del' EEEE d 'de' MMMM 'a las' HH:mm'hs al' ") + end.toString("EEEE d 'de' MMMM 'a las' HH:mm'hs'");
	}
	
	public static String formatDateTime(DateTime dateTime, boolean large) {
		
		if (large) 
			return dateTime.toString("EEEE d 'de' MMMM 'a las' HH:mm");
		else
			return dateTime.toString("EEE d 'de' MMM 'a las' HH:mm");
	}

	public static String formatDuration(Duration duration) {
		return getDayHourMinutesFormatter().print(duration.toPeriod().normalizedStandard());
	}

	/*--------------------------------------------------------------------------------------*/
    /*----------------------------------- Private methods ----------------------------------*/
	
	private static PeriodFormatter getDayHourMinutesFormatter() {
		
		if (dayHourMinutesFormatter == null) {
			
			dayHourMinutesFormatter = new PeriodFormatterBuilder()
				.appendDays().appendSuffix(" dia", " dias").appendSeparator(", ").appendHours()
			    .appendSuffix(" hora", " horas")
			    .appendSeparator(" y ")
			    .appendMinutes()
			    .appendSuffix(" minuto", " minutos")
			    .toFormatter();
		}

		return dayHourMinutesFormatter;
	}

	/*--------------------------------------------------------------------------------------*/
	
}
