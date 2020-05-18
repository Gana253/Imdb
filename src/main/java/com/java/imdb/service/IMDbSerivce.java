package com.java.imdb.service;

import java.util.List;
import java.util.Map;

import com.java.imdb.dto.MovieDTO;
import com.java.imdb.dto.MovieNameWithRatings;

public interface IMDbSerivce {

	/**
	 * Method to fetch the movie details based on the search criteria
	 * 
	 * @param movieName
	 * @return
	 */
	MovieDTO searchMovieByName(String movieName);

	/**
	 * API to retrieve top rated movies
	 * 
	 * @param genre
	 * @return
	 */
	List<MovieNameWithRatings> listTopRatedMoviesByGenre(String genre);

	/**
	 * Method to find the genres the person has type casted and return the count of
	 * movies he acted in each genre
	 * 
	 * @param name
	 * @return
	 */
	Map<String, Integer> typeCastingActors(String name);

	/**
	 * API to fetch the list of movies the person acted together.
	 * 
	 * @param firstName
	 * @param secondName
	 * @return
	 */
	List<MovieDTO> coincidence(String firstName, String secondName);

	/**
	 * Retrieve the bacon number by applying BFS on the graph created
	 * 
	 * @param actor
	 * @return
	 */
	int findBaconNumber(String actor);

}