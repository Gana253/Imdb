/**
 * 
 */
package com.java.imdb.dto;

import java.io.Serializable;

/**
 * @author Ganapathy_N
 *
 */
public class MovieNameWithRatings implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tConst;
	private double averageRating;
	private String primaryTitle;

	private String originalTitle;

	public MovieNameWithRatings(String tConst, double averageRating, String primaryTitle, String originalTitle) {
		super();
		this.tConst = tConst;
		this.averageRating = averageRating;
		this.primaryTitle = primaryTitle;
		this.originalTitle = originalTitle;
	}

	public String gettConst() {
		return tConst;
	}

	public void settConst(String tConst) {
		this.tConst = tConst;
	}

	public double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}

	public String getPrimaryTitle() {
		return primaryTitle;
	}

	public void setPrimaryTitle(String primaryTitle) {
		this.primaryTitle = primaryTitle;
	}

	public String getOriginalTitle() {
		return originalTitle;
	}

	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}

}
