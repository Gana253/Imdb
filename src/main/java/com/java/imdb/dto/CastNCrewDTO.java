/**
 * 
 */
package com.java.imdb.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Ganapathy_N
 *
 */
public class CastNCrewDTO implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	private String tConst;

	private String ordering;

	private String nConst;

	private String category;

	private String job;

	private String characters;

	private String actorsName;
	@JsonIgnore	
	private List<String> knownForTitles;	
	

	public List<String> getKnownForTitles() {
		return knownForTitles;
	}

	public void setKnownForTitles(List<String> knownForTitles) {
		this.knownForTitles = knownForTitles;
	}

	public String getActorsName() {
		return actorsName;
	}

	public void setActorsName(String actorsName) {
		this.actorsName = actorsName;
	}

	public String gettConst() {
		return tConst;
	}

	public void settConst(String tConst) {
		this.tConst = tConst;
	}

	public String getOrdering() {
		return ordering;
	}

	public void setOrdering(String ordering) {
		this.ordering = ordering;
	}

	public String getnConst() {
		return nConst;
	}

	public void setnConst(String nConst) {
		this.nConst = nConst;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getCharacters() {
		return characters;
	}

	public void setCharacters(String characters) {
		this.characters = characters;
	}

	@Override
	public String toString() {
		return "CastNCrewDTO [tConst=" + tConst + ", ordering=" + ordering + ", nConst=" + nConst + ", category="
				+ category + ", job=" + job + ", characters=" + characters + ", actorsName=" + actorsName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actorsName == null) ? 0 : actorsName.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((characters == null) ? 0 : characters.hashCode());
		result = prime * result + ((job == null) ? 0 : job.hashCode());
		result = prime * result + ((nConst == null) ? 0 : nConst.hashCode());
		result = prime * result + ((ordering == null) ? 0 : ordering.hashCode());
		result = prime * result + ((tConst == null) ? 0 : tConst.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CastNCrewDTO other = (CastNCrewDTO) obj;
		if (actorsName == null) {
			if (other.actorsName != null)
				return false;
		} else if (!actorsName.equals(other.actorsName))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (characters == null) {
			if (other.characters != null)
				return false;
		} else if (!characters.equals(other.characters))
			return false;
		if (job == null) {
			if (other.job != null)
				return false;
		} else if (!job.equals(other.job))
			return false;
		if (nConst == null) {
			if (other.nConst != null)
				return false;
		} else if (!nConst.equals(other.nConst))
			return false;
		if (ordering == null) {
			if (other.ordering != null)
				return false;
		} else if (!ordering.equals(other.ordering))
			return false;
		if (tConst == null) {
			if (other.tConst != null)
				return false;
		} else if (!tConst.equals(other.tConst))
			return false;
		return true;
	}

}
