package com.turnolibre.util;

import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

public class TestCollectionUtils {

	@Test
	public void shouldPreserveOrder() {
		
		Set<Integer> integerSet = new TreeSet<Integer>();
		integerSet.add(1);
		integerSet.add(2);
		integerSet.add(3);
		
		List<Integer> integerList = CollectionUtils.asList(integerSet);
		assertEquals(1, integerList.get(0).intValue());
		assertEquals(2, integerList.get(1).intValue());
		assertEquals(3, integerList.get(2).intValue());
	}
	
}
