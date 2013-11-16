package util;

import com.turnolibre.business.joda.time.DayOfWeekTime;
import org.joda.time.*;


public class TestConstants {
	
	public static final long CURRENT_TIME_MOCK = new DateTime(2012, 1, 12, 6, 0).getMillis();

	public static final Long USUARIO_VICTOR_ID = 1L;
	public static final Long USUARIO_MANUEL_ID = 2L;

	public static final Long CLIENTE_VICTOR_ID = 1L;
	public static final Long CLIENTE_MANUEL_ID = 2L;
	public static final Long CLIENTE_JUAN_ID   = 3L;
	public static final Long CLIENTE_CARLOS_ID = 4L;
	
	public static final Long HORARIO_CON_MAS_DE_UN_TURNO_LIBRE_ID = 1L;
	public static final Long HORARIO_CON_ULTIMO_TURNO_LIBRE_ID    = 73L;
	public static final Long HORARIO_SIN_TURNOS_TOMADOS_ID        = 1L;
	public static final Long HORARIO_CON_TURNOS_TOMADOS_ID        = 73L;
	
	public static final Long TURNO_DE_VICTOR_ID = 153L;
	public static final Long ADM_DE_AGENDA_2_ID = 6L;
	public static final Long _9_DE_JULIO_ID = 2L;
	public static final Long VIERNES_20_DE_ENERO_ID = 1L;
	public static final Long AGENDA_DRA_LOPEZ_ID = 2L;
	public static final Long JORNADA_HABITUAL_DRA_LOPEZ_ID = 2L;
	public static final Long JORNADA_DEL_18_DE_MARZO_ID = 1L;
	
	public static final LocalDate _1_DE_ENERO_DEL_2012 = new LocalDate(2012, 1, 1);
	public static final LocalDate _20_DE_ENERO_DEL_2012 = new LocalDate(2012, 1, 20);
	public static final LocalDate _18_DE_MARZO_DEL_2012 = new LocalDate(2012, 3, 18);
	public static final LocalDate _1_DE_MAYO_DEL_2012 = new LocalDate(2012, 5, 1);
	public static final LocalDate _25_DE_MAYO_DEL_2012 = new LocalDate(2012, 5, 25);
	public static final LocalDate _9_DE_JULIO_DEL_2012 = new LocalDate(2012, 7, 9);
	public static final LocalDate _29_DE_OCTUBRE_DEL_2013 = new LocalDate(2013, 10, 29);
	public static final LocalDate _31_DE_DICIEMBRE_DEL_2012 = new LocalDate(2012, 12, 31);

	public static final DayOfWeekTime LUNES_A_LAS_09 = new DayOfWeekTime(DateTimeConstants.MONDAY, 9, 0);
	public static final DayOfWeekTime LUNES_A_LAS_10 = new DayOfWeekTime(DateTimeConstants.MONDAY, 10, 0);
	public static final DayOfWeekTime LUNES_A_LAS_12 = new DayOfWeekTime(DateTimeConstants.MONDAY, 12, 0);
	public static final DayOfWeekTime LUNES_A_LAS_15 = new DayOfWeekTime(DateTimeConstants.MONDAY, 15, 0);
	public static final DayOfWeekTime LUNES_A_LAS_17_30 = new DayOfWeekTime(DateTimeConstants.MONDAY, 17, 30);
	public static final DayOfWeekTime LUNES_A_LAS_18 = new DayOfWeekTime(DateTimeConstants.MONDAY, 18, 0);
	public static final DayOfWeekTime LUNES_A_LAS_20 = new DayOfWeekTime(DateTimeConstants.MONDAY, 20, 0);
	public static final DayOfWeekTime MARTES_A_LAS_09 = new DayOfWeekTime(DateTimeConstants.TUESDAY, 9, 0);
	public static final DayOfWeekTime MARTES_A_LAS_10 = new DayOfWeekTime(DateTimeConstants.TUESDAY, 10, 0);
	public static final DayOfWeekTime MARTES_A_LAS_11 = new DayOfWeekTime(DateTimeConstants.TUESDAY, 11, 0);
	public static final DayOfWeekTime MARTES_A_LAS_12 = new DayOfWeekTime(DateTimeConstants.TUESDAY, 12, 0);
	public static final DayOfWeekTime MARTES_A_LAS_14_30 = new DayOfWeekTime(DateTimeConstants.TUESDAY, 14, 30);
	public static final DayOfWeekTime MARTES_A_LAS_18 = new DayOfWeekTime(DateTimeConstants.TUESDAY, 18, 0);
	public static final DayOfWeekTime MARTES_A_LAS_20_30 = new DayOfWeekTime(DateTimeConstants.TUESDAY, 20, 30);
	public static final DayOfWeekTime MIERCOLES_A_LAS_09 = new DayOfWeekTime(DateTimeConstants.WEDNESDAY, 9, 0);
	public static final DayOfWeekTime MIERCOLES_A_LAS_10 = new DayOfWeekTime(DateTimeConstants.WEDNESDAY, 10, 0);
	public static final DayOfWeekTime MIERCOLES_A_LAS_18 = new DayOfWeekTime(DateTimeConstants.WEDNESDAY, 18, 0);
	public static final DayOfWeekTime JUEVES_A_LAS_09 = new DayOfWeekTime(DateTimeConstants.THURSDAY, 9, 0);
	public static final DayOfWeekTime JUEVES_A_LAS_10 = new DayOfWeekTime(DateTimeConstants.THURSDAY, 10, 0);
	public static final DayOfWeekTime JUEVES_A_LAS_18 = new DayOfWeekTime(DateTimeConstants.THURSDAY, 18, 0);
	public static final DayOfWeekTime VIERNES_A_LAS_09 = new DayOfWeekTime(DateTimeConstants.FRIDAY, 9, 0);
	public static final DayOfWeekTime VIERNES_A_LAS_10 = new DayOfWeekTime(DateTimeConstants.FRIDAY, 10, 0);
	public static final DayOfWeekTime VIERNES_A_LAS_15 = new DayOfWeekTime(DateTimeConstants.FRIDAY, 15, 0);
	public static final DayOfWeekTime VIERNES_A_LAS_18 = new DayOfWeekTime(DateTimeConstants.FRIDAY, 18, 0);
	public static final DayOfWeekTime SABADO_A_LAS_09 = new DayOfWeekTime(DateTimeConstants.SATURDAY, 9, 0);
	public static final DayOfWeekTime SABADO_A_LAS_10 = new DayOfWeekTime(DateTimeConstants.SATURDAY, 10, 0);
	public static final DayOfWeekTime SABADO_A_LAS_13 = new DayOfWeekTime(DateTimeConstants.SATURDAY, 13, 0);
	public static final DayOfWeekTime SABADO_A_LAS_15 = new DayOfWeekTime(DateTimeConstants.SATURDAY, 15, 0);
	public static final DayOfWeekTime DOMINGO_A_LAS_12 = new DayOfWeekTime(DateTimeConstants.SUNDAY, 12, 0);
	public static final DayOfWeekTime DOMINGO_A_LAS_15 = new DayOfWeekTime(DateTimeConstants.SUNDAY, 15, 0);
	
	public static final int _1_TURNO = 1;
	public static final int _2_TURNOS = 2;
	public static final int _3_TURNOS = 3;
	
	public static final Duration _20_MINUTOS = Duration.standardMinutes(20);
	public static final Duration _30_MINUTOS = Duration.standardMinutes(30);
	public static final Duration _1_HORA = Duration.standardHours(1);
	public static final Duration _1_30_HORAS = Duration.standardHours(1).plus(Duration.standardMinutes(30));
	public static final Duration _2_HORAS = Duration.standardHours(2);
		
	public static final Period ANTELACION_60_DIAS = Period.days(60);
	public static final Period ANTELACION_1_MES   = Period.months(1);
	public static final Period ANTELACION_2_MESES = Period.months(2);
	public static final Period PERIODO_1_SEMANA = Period.weeks(1);

	public static final DateTime _9_ENE_2012_A_LAS_10 = new DateTime(2013, 1, 9, 10, 0);
	public static final DateTime _9_ENE_2012_A_LAS_10_9 = new DateTime(2013, 1, 9, 10, 9);
	public static final DateTime _9_ENE_2012_A_LAS_18 = new DateTime(2013, 1, 9, 18, 0);
	public static final DateTime _9_ENE_2012_A_LAS_20 = new DateTime(2012, 1, 9, 20, 0);
	public static final DateTime _9_ENE_2012_A_LAS_22 = new DateTime(2012, 1, 9, 22, 0);
	public static final DateTime _12_ENE_2012_A_LAS_6 = new DateTime(2012, 1, 12, 6, 0);
	public static final DateTime _16_ENE_2012_A_LAS_11 = new DateTime(2012, 1, 16, 11, 0);
	public static final DateTime _16_ENE_2012_A_LAS_13 = new DateTime(2012, 1, 16, 13, 0);
	public static final DateTime _16_ENE_2012_A_LAS_14 = new DateTime(2012, 1, 16, 14, 0);
	public static final DateTime _16_ENE_2012_A_LAS_16 = new DateTime(2012, 1, 16, 16, 0);
	public static final DateTime _16_ENE_2012_A_LAS_17 = new DateTime(2012, 1, 16, 17, 0);
	public static final DateTime _16_ENE_2012_A_LAS_17_30 = new DateTime(2012, 1, 16, 17, 30);
	public static final DateTime _16_ENE_2012_A_LAS_10 = new DateTime(2012, 1, 16, 10, 0);
	public static final DateTime JUE_19_ENE_2012_A_LAS_17 = new DateTime(2012, 1, 19, 0, 0);
	public static final DateTime _25_FEB_2012_A_LAS_10 = new DateTime(2012, 2, 25, 10, 0);
	public static final DateTime _25_FEB_2012_A_LAS_12 = new DateTime(2012, 2, 25, 12, 0);
	public static final DateTime _25_FEB_2012_A_LAS_13 = new DateTime(2012, 2, 25, 13, 0);
	public static final DateTime _25_FEB_2012_A_LAS_14 = new DateTime(2012, 2, 25, 14, 0);
	public static final DateTime _25_FEB_2012_A_LAS_15 = new DateTime(2012, 2, 25, 15, 0);
	public static final DateTime _25_FEB_2012_A_LAS_17 = new DateTime(2012, 2, 25, 17, 0);
	public static final DateTime _25_FEB_2012_A_LAS_18 = new DateTime(2012, 2, 25, 18, 0);
	public static final DateTime _25_FEB_2012_A_LAS_20 = new DateTime(2012, 2, 25, 20, 0);
	public static final DateTime DOM_18_MARZO_2012_A_LAS_10 = new DateTime(2012, 3, 18, 10, 0);
	public static final DateTime DOM_18_MARZO_2012_A_LAS_17 = new DateTime(2012, 3, 18, 17, 0);
	public static final DateTime LUN_10_DIC_2012_A_LAS_10 = new DateTime(2013, 12, 10, 10, 0);
	public static final DateTime LUN_10_DIC_2012_A_LAS_18 = new DateTime(2013, 12, 10, 18, 0);
	public static final DateTime _22_DIC_2012_A_LAS_10 = new DateTime(2012, 12, 22, 10, 0);
	public static final DateTime _22_DIC_2012_A_LAS_11 = new DateTime(2012, 12, 22, 11, 0);
	public static final DateTime _22_DIC_2012_A_LAS_15 = new DateTime(2012, 12, 22, 15, 0);
	public static final DateTime _22_DIC_2012_A_LAS_17 = new DateTime(2012, 12, 22, 17, 0);
	public static final DateTime _22_DIC_2012_A_LAS_18 = new DateTime(2012, 12, 22, 18, 0);
		
	public static final Interval INTERVALO_EN_JORNADA_HABITUAL = new Interval(new DateTime(2012, 1, 23, 10, 0), new DateTime(2012, 1, 23, 11, 0));
	
	public static final String MOTIVO_DE_DESHABILITACION = "Urgencia familiar";

}
