package com.turnolibre.business.agenda;

import com.turnolibre.business.excepcion.ExcepcionDeReglaDelNegocio;
import com.turnolibre.business.turno.Horario;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Clase utilitaria encargada de filtrar horarios bajo determinados criterios de busqueda.
 *
 * @author Victor Del Rio
 */
public class FiltroDeHorarios {

	
	/*------------------------------------ Public methods ----------------------------------*/

	public static SortedSet<Horario> ejecutar(SortedSet<Horario> horarios, DateTime desde, DateTime hasta) {
		return ejecutar(horarios, new Interval(desde, hasta));
	}
	
	public static SortedSet<Horario> ejecutar(SortedSet<Horario> horarios, Interval intervalo) {
		
		SortedSet<Horario> horariosFiltrados = new TreeSet<Horario>();
		
		if ( !horarios.isEmpty() ) {
			
			intervalo = ajustarIntervalo(intervalo, horarios);
			
			try {
				
				Horario primerHorarioDelIntervalo = new Horario(new Interval(intervalo.getStart(), Horario.getDuracionMinima()), 1);
				Horario ultimoHorarioDelIntervalo = new Horario(new Interval(intervalo.getEnd(), Horario.getDuracionMinima()), 1);
				
				try {
					horariosFiltrados.addAll(horarios.subSet(primerHorarioDelIntervalo, ultimoHorarioDelIntervalo));
				} catch (IllegalArgumentException e) {
					// O fromElement o toElement estaban fuera del rango, se devuelve un set vacio de horarios
				}
				
			} catch (ExcepcionDeReglaDelNegocio e) {
				// Esta excepcion no es de interez para el cliente del metodo y nunca va a ocurrir
			}
		}
		
		return horariosFiltrados;
	}
	
	public static SortedSet<Horario> ejecutar(SortedSet<Horario> horarios, Set<Interval> intervalos) {
		
		SortedSet<Horario> horariosFiltrados = new TreeSet<Horario>();

		for (Interval intervalo : intervalos)
			horariosFiltrados.addAll(ejecutar(horarios, intervalo));
		
		return horariosFiltrados;
	}
	
	public static boolean contieneHorarios(SortedSet<Horario> horarios, Interval intervalo) {
		return !ejecutar(horarios, intervalo).isEmpty();
	}

	/*--------------------------------------------------------------------------------------*/
	/*----------------------------------- Private methods ----------------------------------*/

	// A esto hay que hacerlo para poder filtrar por intervalos que no esten completamente dentro
	// del rango del NavigableSet
	private static Interval ajustarIntervalo(Interval intervalo, SortedSet<Horario> horarios) {
		
		Interval intervaloAjustado = intervalo;
		
		DateTime inicioDelIntervaloAFiltrar = intervalo.getStart();
		DateTime finDelIntervaloAFiltrar = intervalo.getEnd();
		DateTime inicioDelPrimerHorario = horarios.first().getIntervalo().getStart();
		DateTime finDelUltimoHorario = horarios.last().getIntervalo().getEnd();
		
		if ( !intervaloComplentamenteFueraDeRango(inicioDelIntervaloAFiltrar, finDelIntervaloAFiltrar, inicioDelPrimerHorario, finDelUltimoHorario) ) {
			
			if ( inicioDelIntervaloAFiltrar.isBefore(inicioDelPrimerHorario) )
				inicioDelIntervaloAFiltrar = inicioDelPrimerHorario;
			
			if ( finDelIntervaloAFiltrar.isAfter(finDelUltimoHorario) )
				finDelIntervaloAFiltrar = finDelUltimoHorario;
			
			intervaloAjustado = new Interval(inicioDelIntervaloAFiltrar, finDelIntervaloAFiltrar);
		}
		
		return intervaloAjustado;
	}
	
	private static boolean intervaloComplentamenteFueraDeRango(DateTime inicioDelIntervaloAFiltrar, DateTime finDelIntervaloAFiltrar, DateTime inicioDelPrimerHorario, DateTime finDelUltimoHorario) {
		return finDelIntervaloAFiltrar.isBefore(inicioDelPrimerHorario) || inicioDelIntervaloAFiltrar.isAfter(finDelUltimoHorario);
	}

	/*--------------------------------------------------------------------------------------*/

}
