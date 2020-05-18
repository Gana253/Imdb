/**
 * 
 */
package com.java.imdb.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ganapathy_N
 *
 */
public class ActorsDTO {
	private String name;
	private List<String> movies = new ArrayList<String>();
	private int distance;
	private ActorsDTO prev = null;

	public ActorsDTO(String name) {
		super();
		this.name = name;
	}

	public ActorsDTO getPrev() {
		return prev;
	}

	public void setPrev(ActorsDTO prev) {
		this.prev = prev;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getMovies() {
		return movies;
	}

	public void setMovies(List<String> movies) {
		this.movies = movies;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

}
