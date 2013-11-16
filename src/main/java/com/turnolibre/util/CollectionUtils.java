package com.turnolibre.util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Set;

public class CollectionUtils {

	public static <T> List<T> asList(Set<T> set) {
		return Lists.newArrayList(set);
	}

	public static <T> T get(Iterable<T> iterable, T element) {
		return get(iterable, Predicates.equalTo(element));
	}
	
	public static <T> T get(Iterable<T> iterable, Predicate<? super T> predicate) {
		return Iterables.find(iterable, predicate, null);
	}
	
	public static <T> boolean contains(Iterable<T> iterable, Predicate<? super T> predicate) {
		return get(iterable, predicate) != null;
	}
	
}
