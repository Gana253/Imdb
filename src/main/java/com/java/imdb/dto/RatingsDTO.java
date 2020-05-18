/**
 * 
 */
package com.java.imdb.dto;

import java.io.Serializable;

/**
 * @author Ganapathy_N
 *
 */
public class RatingsDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String tConst;
	
	private String averageRating;
	
	private String numVotes;

	public String gettConst() {
		return tConst;
	}

	public void settConst(String tConst) {
		this.tConst = tConst;
	}

	public String getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(String averageRating) {
		this.averageRating = averageRating;
	}

	public String getNumVotes() {
		return numVotes;
	}

	public void setNumVotes(String numVotes) {
		this.numVotes = numVotes;
	}
	
	
}
