package com.turnolibre;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		AcceptanceTests.class,
		PresentationTests.class,
		ServiceTests.class,
		UnitTests.class
})
public class AllTests {

}