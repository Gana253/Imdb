/**
 * 
 */
package com.java.imdb.helper.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Ganapathy_N
 *
 */
public class IMDbHelper {
	public static final String MOVIE = "movie";
	public static final String TAB = "\\t";
	public static final String SPACE = " ";
	public static final String TIDE_CONSTANT = "~";
	

	/**
	 * Method to split the search value and return back keyword without specila
	 * character
	 * 
	 * @param inputSearchValue
	 * @return
	 */
	public static String retrieveKeyWordToSearch(String inputSearchValue) {
		// Split the inputSearchvalue passed through UI/Endpoint
		List<String> inputSearchValueLst = Stream.of(inputSearchValue.split(TIDE_CONSTANT))
				.map(elem -> new String(elem)).collect(Collectors.toList());

		// To get the keyword which has maximum letters to make search more precise
		Optional<String> keyWordToSearch = inputSearchValueLst.stream().max(Comparator.comparingInt(String::length));

		// Remove the special characters if any
		String keyWordToSearchExcSplChar = keyWordToSearch.get().replaceAll("[^a-zA-Z0-9 ]", "");

		return keyWordToSearchExcSplChar;
	}
	
	/**
	 * Method to count the occurence of the word
	 * 
	 * @param list
	 * @return
	 */
	public static Map<String, Integer> countFrequencies(List<String> list) {

		// hash set is created and elements of
		// arraylist are insertd into it
		Set<String> st = new HashSet<String>(list);

		Map<String, Integer> result = new HashMap<String, Integer>();
		for (String s : st) {
			result.put(s, Collections.frequency(list, s));
		}
		return result;
	}
	
	
}
