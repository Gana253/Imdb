/**
 * 
 */
package com.java.imdb.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Ganapathy_N
 *
 */
public class MovieDTO implements Serializable {

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	private String tConst;

	private String titleType;

	private String primaryTitle;

	private String originalTitle;

	private Boolean isAdult;

	private String startYear;

	private String endYear;

	private String runtime;

	private List<String> genres;

	@JsonProperty("cast_crew")
	private Map<String, String> castNCrew;

	public Map<String, String> getCastNCrew() {
		return castNCrew;
	}

	public void setCastNCrew(Map<String, String> castNCrew) {
		this.castNCrew = castNCrew;
	}

	public String gettConst() {
		return tConst;
	}

	public void settConst(String tConst) {
		this.tConst = tConst;
	}

	public String getTitleType() {
		return titleType;
	}

	public void setTitleType(String titleType) {
		this.titleType = titleType;
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

	public Boolean getIsAdult() {
		return isAdult;
	}

	public void setIsAdult(Boolean isAdult) {
		this.isAdult = isAdult;
	}

	public String getStartYear() {
		return startYear;
	}

	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}

	public String getEndYear() {
		return endYear;
	}

	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public List<String> getGenres() {
		return genres;
	}

	public void setGenres(List<String> genres) {
		this.genres = genres;
	}

	@Override
	public String toString() {
		return "MovieDTO [tConst=" + tConst + ", titleType=" + titleType + ", primaryTitle=" + primaryTitle
				+ ", originalTitle=" + originalTitle + ", isAdult=" + isAdult + ", startYear=" + startYear
				+ ", endYear=" + endYear + ", runtime=" + runtime + ", genres=" + genres
				+ ", castNCrew=" + castNCrew + "]";
	}

}
