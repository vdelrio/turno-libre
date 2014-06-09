package com.turnolibre;

import com.turnolibre.business.agenda.TestAgenda;
import com.turnolibre.business.agenda.TestFiltroDeHorariosPorIntervalo;
import com.turnolibre.business.agenda.TestJornadaLaboralHabitual;
import com.turnolibre.business.agenda.TestJornadaLaboralOcasional;
import com.turnolibre.business.turno.TestHorario;
import com.turnolibre.util.TestCollectionUtils;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		TestAgenda.class,
		TestFiltroDeHorariosPorIntervalo.class,
		TestJornadaLaboralHabitual.class,
		TestJornadaLaboralOcasional.class,
		TestHorario.class,
		TestCollectionUtils.class
})
public class UnitTests {

}

