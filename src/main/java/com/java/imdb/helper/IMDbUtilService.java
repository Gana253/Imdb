package com.java.imdb.helper;

import java.util.List;

import com.java.imdb.dto.CastNCrewDTO;
import com.java.imdb.dto.MovieDTO;
import com.java.imdb.dto.NameDTO;
import com.java.imdb.dto.RatingsDTO;

public interface IMDbUtilService {

	/**
	 * Retrieve the List of person names from the namedata tsv based on the search
	 * criteria
	 * 
	 * @param resource
	 * @param keyWordToSearchExcSplChar
	 * @param searchResult
	 * @return
	 */
	NameDTO retrieveCastName(String keyWordToSearchExcSplChar, String movieId, Boolean isBaconSearch);

	/**
	 * 
	 * API to retrieve list of movies,tvSeries,tvMiniSeries ..etc from tsv based on
	 * the search criteria
	 * 
	 * @param resource
	 * @param result
	 * @param keyWordToSearchExcSplChar
	 * @return
	 */
	List<MovieDTO> fetchRecordsFromBaseTitle(String keyWordToSearchExcSplChar);

	/**
	 * To retrieve cast and crew members for the movie
	 * 
	 * @param op
	 * @return
	 */
	List<CastNCrewDTO> retrieveCastNCrewForMovie(String movieDTO, Boolean isBaconSearch);

	/**
	 * Retrieve the records from rating tsv file on startup
	 * 
	 * @return
	 */
	List<RatingsDTO> retrieveRecordsFromRatingTsv();

	/**
	 * To fetch the movie data from the tsv file - Only movie records will be
	 * fetched
	 * 
	 * @param isBaconSearch
	 * @return
	 */
	List<MovieDTO> fetchMovieRecords(Boolean isBaconSearch);

}