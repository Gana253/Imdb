/**
 * 
 */
package com.java.imdb.dto;

import java.io.Serializable;

/**
 * @author Ganapathy_N
 *
 */
public class NameDTO implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private String nConst;

private String primaryName;

private String birthYear;

private String deathYear;

private String[] PrimaryProfession;

private String[] knownForTitles;

public String getnConst() {
	return nConst;
}

public void setnConst(String nConst) {
	this.nConst = nConst;
}

public String getPrimaryName() {
	return primaryName;
}

public void setPrimaryName(String primaryName) {
	this.primaryName = primaryName;
}

public String getBirthYear() {
	return birthYear;
}

public void setBirthYear(String birthYear) {
	this.birthYear = birthYear;
}

public String getDeathYear() {
	return deathYear;
}

public void setDeathYear(String deathYear) {
	this.deathYear = deathYear;
}

public String[] getPrimaryProfession() {
	return PrimaryProfession;
}

public void setPrimaryProfession(String[] primaryProfession) {
	PrimaryProfession = primaryProfession;
}

public String[] getKnownForTitles() {
	return knownForTitles;
}

public void setKnownForTitles(String[] knownForTitles) {
	this.knownForTitles = knownForTitles;
}

@Override
public String toString() {
	return "NameDTO [nConst=" + nConst + ", primaryName=" + primaryName + ", birthYear=" + birthYear + ", deathYear="
			+ deathYear + ", PrimaryProfession=" + PrimaryProfession + ", knownForTitles=" + knownForTitles + "]";
}


}
