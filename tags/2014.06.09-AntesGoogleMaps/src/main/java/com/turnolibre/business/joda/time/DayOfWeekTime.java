package com.turnolibre.business.joda.time;

import org.joda.time.LocalTime;

import java.io.Serializable;

/**
 * Representa a un día de la semana en un determinado instante.
 * Ej.: lunes a las 9:30.
 *
 * @author Victor Del Rio
 */
public class DayOfWeekTime implements Serializable, Comparable<DayOfWeekTime> {

	private static final long serialVersionUID = 1L;
	
	private Integer dayOfWeek;
	private LocalTime time;
	
	
	/*------------------------------------ Constructors ------------------------------------*/

	protected DayOfWeekTime() {
		super();
	}
	
	public DayOfWeekTime(Integer dayOfWeek, LocalTime time) {
		
		this.dayOfWeek = dayOfWeek;
		this.time = time;
	}
	
	public DayOfWeekTime(Integer dayOfWeek, Integer hourOfDay, Integer minuteOfHour) {
		
		this.dayOfWeek = dayOfWeek;
		this.time = new LocalTime(hourOfDay, minuteOfHour);
	}

	/*--------------------------------------------------------------------------------------*/
	/*---------------------------------- Geters and seters ---------------------------------*/

	public Integer getDayOfWeek() {
		return dayOfWeek;
	}
	
	public Integer getHourOfDay() {
		return time.getHourOfDay();
	}
	
	public Integer getMinuteOfHour() {
		return time.getMinuteOfHour();
	}

	public LocalTime getTime() {
		return time;
	}

	/*--------------------------------------------------------------------------------------*/
	/*-------------------------------- Hash code and equals --------------------------------*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dayOfWeek == null) ? 0 : dayOfWeek.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DayOfWeekTime other = (DayOfWeekTime) obj;
		if (dayOfWeek == null) {
			if (other.dayOfWeek != null)
				return false;
		} else if (!dayOfWeek.equals(other.dayOfWeek))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}

	@Override
	public int compareTo(DayOfWeekTime otro) {
		
		if ( this.dayOfWeek < otro.dayOfWeek )
			return -1;
		else if ( this.dayOfWeek > otro.dayOfWeek )
			return 1;
		else 
			return this.time.compareTo(otro.time);
	}

	/*--------------------------------------------------------------------------------------*/
	/*-------------------------------------- Overrides -------------------------------------*/

	@Override
	public String toString() {
		return dayOfWeek.toString() + " - " + time.toString();
	}

	/*--------------------------------------------------------------------------------------*/

}
