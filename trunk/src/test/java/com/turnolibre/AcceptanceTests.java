package com.turnolibre;

import com.turnolibre.aceptacion.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	ClienteCancelaTurnos.class,
	ClienteSacaTurnos.class,
	PrestadorCancelaTurnos.class,
	PrestadorDefineDiasNoLaborales.class,
	PrestadorDefineJornadasLaborales.class
})
public class AcceptanceTests {

}
