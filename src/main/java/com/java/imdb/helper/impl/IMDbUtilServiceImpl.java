/**
 * 
 */
package com.java.imdb.helper.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.java.imdb.dto.CastNCrewDTO;
import com.java.imdb.dto.MovieDTO;
import com.java.imdb.dto.NameDTO;
import com.java.imdb.dto.RatingsDTO;
import com.java.imdb.helper.IMDbUtilService;

/**
 * Utility class for IMDb Service 
 * 
 * @author Ganapathy_N
 *
 */
@Service("imdbUtilService")
public class IMDbUtilServiceImpl implements IMDbUtilService {

	private static final String ACTRESS = "actress";

	private static final String ACTOR = "actor";

	private static final Logger log = LoggerFactory.getLogger(IMDbUtilServiceImpl.class);

	@Autowired
	private ResourceLoader resourceLoader;

	/**
	 * 
	 * Build Movie object with retrieve line from tsv
	 * 
	 * @param data
	 * @param titleBasicDTO
	 */
	private void buildMovieObjectWithParsedData(String[] data, MovieDTO titleBasicDTO) {
		for (int i = 0; i < data.length; i++) {
			switch (i) {
			case 0:
				titleBasicDTO.settConst(data[i]);
				break;

			case 1:
				titleBasicDTO.setTitleType(data[i]);
				break;
			case 2:
				titleBasicDTO.setPrimaryTitle(data[i]);
				break;

			case 3:
				titleBasicDTO.setOriginalTitle(data[i]);
				break;

			case 4:
				titleBasicDTO.setIsAdult(data[i].equals("0") ? false : true);
				break;

			case 5:
				titleBasicDTO.setStartYear(data[i].equals("\\N") ? "NA" : data[i]);
				break;

			case 6:
				titleBasicDTO.setEndYear(data[i].equals("\\N") ? "NA" : data[i]);
				break;

			case 7:

				titleBasicDTO.setRuntime(data[i].equals("\\N") ? "NA" : data[i]);
				break;
			default:
				titleBasicDTO.setGenres(Arrays.asList(data[i].split(",")));
				break;
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lunatech.imdb.helper.IMDbHelperUtil#retrieveCastName(java.lang.String)
	 */
	@Override
	public NameDTO retrieveCastName(String nameConst, String movieId, Boolean isBaconSearch) {
		log.info("Retrieve Name list with movies acted--{}", nameConst);

		List<String> searchResult = null;

		searchResult = fetchNameDetails(nameConst, movieId, isBaconSearch);
		if (searchResult.isEmpty()) {
			return null;
		}
		/*
		 * Build the Name object for each search results
		 */
		String[] data = searchResult.get(0).split(IMDbHelper.TAB);
		NameDTO nameDTO = new NameDTO();
		for (int i = 0; i < data.length; i++) {
			switch (i) {
			case 0:
				nameDTO.setnConst(data[i]);
				break;

			case 1:
				nameDTO.setPrimaryName(data[i]);
				break;
			case 2:
				nameDTO.setBirthYear(data[i]);
				break;

			case 3:
				nameDTO.setDeathYear(data[i].equals("\\N") ? "NA" : data[i]);
				break;

			case 4:
				nameDTO.setPrimaryProfession(data[i].split(","));
				break;

			default:
				nameDTO.setKnownForTitles(data[i].split(","));
				break;
			}

		}
		return nameDTO;

	}

	/**
	 * @param nameConst
	 * @param movieId
	 * @param resource
	 * @param searchResult
	 * @return
	 */
	private List<String> fetchNameDetails(String nameConst, String movieId, Boolean isBaconSearch) {
		Resource resource = resourceLoader.getResource("classpath:dataset/namedata.tsv");
		List<String> searchResult = null;
		File file = null;
		try {
			file = resource.getFile();
			// Search Line By Line in the tsv file
			if (!isBaconSearch && null != movieId) {
				searchResult = Files.lines(file.toPath())
						.filter(line -> line.startsWith(nameConst) && line.contains(movieId))
						.collect(Collectors.toList());
			} else {
				searchResult = Files.lines(file.toPath()).filter(line -> line.contains(nameConst))
						.collect(Collectors.toList());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return searchResult;
	}

	/**
	 * Search API to find the cast/crew members based on the movieid from tsv file
	 * 
	 * @param movieId
	 * @return
	 */
	private List<CastNCrewDTO> retrieveCastNCrew(String movieId, Boolean isBaconSearch) {
		log.info("Fetch crew members matching the movie id --{}", movieId);

		List<String> castNCrewSearchResult = null;
		castNCrewSearchResult = fetchCastNCrewFromPrincipalData(movieId, isBaconSearch);

		List<CastNCrewDTO> castNCrewLst = new LinkedList<CastNCrewDTO>();
		// Build the cast crew object based on the retrieved line from tsv
		try (Stream<String> stream = castNCrewSearchResult.stream()) {

			castNCrewLst = stream.map(line -> line.split(IMDbHelper.TAB)).map(data -> {

				CastNCrewDTO castNCrew = new CastNCrewDTO();

				for (int i = 0; i < data.length; i++) {
					switch (i) {
					case 0:
						castNCrew.settConst(data[i]);
						break;

					case 1:
						castNCrew.setOrdering(data[i]);
						break;
					case 2:
						castNCrew.setnConst(data[i]);
						break;

					case 3:
						castNCrew.setCategory(data[i].equals("\\N") ? "NA" : data[i]);
						break;

					case 4:
						castNCrew.setJob(data[i].equals("\\N") ? "NA" : data[i]);
						break;

					default:
						castNCrew.setCharacters(data[i].equals("\\N") ? "NA" : data[i]);
						break;
					}

				}
				return castNCrew;
			}).collect(Collectors.toList());
		}

		return castNCrewLst;
	}

	/**
	 * To fetch records from principalsData.tsv
	 * If isBaconSearch is false -> Search will be carried out only with movieId
	 * If isBaconSearch is true -> Search will be carried out with movieId and type - actor,actress
	 * 
	 * @param movieId
	 * @param castNCrewSearchResult
	 * @return
	 */
	private List<String> fetchCastNCrewFromPrincipalData(String movieId, Boolean isBaconSearch) {
		Resource resource = resourceLoader.getResource("classpath:dataset/principalsdata.tsv");

		File file = null;
		List<String> castNCrewSearchResult = null;
		try {
			file = resource.getFile();

			try (Stream<String> stream = Files.lines(file.toPath())) {
				if (!isBaconSearch) {
					// filter line(based on the movieid ) and convert it into a List
					castNCrewSearchResult = stream.filter(line -> line.startsWith(movieId) && line.contains(movieId))
							.collect(Collectors.toList());
				} else {
					// filter line(based on the movieid ) and convert it into a List
					castNCrewSearchResult = stream.filter(
							line -> (line.startsWith(movieId) && (line.contains(ACTOR) || line.contains(ACTRESS))))
							.collect(Collectors.toList());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return castNCrewSearchResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lunatech.imdb.helper.IMDbHelperUtil#fetchRecordsFromBaseTitle(java.lang.
	 * String)
	 */
	@Override
	public List<MovieDTO> fetchRecordsFromBaseTitle(String keyWordToSearchExcSplChar) {
		log.info("Retrieve Movie list matchin the criteria--{}", keyWordToSearchExcSplChar);

		List<MovieDTO> result = new ArrayList<MovieDTO>();
		// Load the file from the classpath
		Resource resource = resourceLoader.getResource("classpath:dataset/moviedata.tsv");
		try {

			File file = resource.getFile();
			// Search in file line by line having the search keyword
			List<String> searchResult = Files.lines(file.toPath())
					.filter(line -> line.contains(keyWordToSearchExcSplChar)).collect(Collectors.toList());
			// Iterate the searchResult to build the movie list
			try (Stream<String> stream = searchResult.stream()) {
				// For Each Split by tab
				result = stream.map(line -> line.split(IMDbHelper.TAB)).map(data -> {
					MovieDTO titleBasicDTO = new MovieDTO();
					// Build Movie DTO
					buildMovieObjectWithParsedData(data, titleBasicDTO);

					return titleBasicDTO;
				}).collect(Collectors.toList());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lunatech.imdb.helper.IMDbHelperUtil#retrieveCastNCrewForMovie(com.
	 * lunatech.imdb.domain.MovieDTO)
	 */
	@Override
	public List<CastNCrewDTO> retrieveCastNCrewForMovie(String movied, Boolean isBaconSearch) {
		log.info("Retrieve cast and crew member for movie --{}");

		List<CastNCrewDTO> castCrewLst = retrieveCastNCrew(null != movied ? movied : null, isBaconSearch);

		try (Stream<CastNCrewDTO> stream = castCrewLst.stream()) {
			castCrewLst = stream.map(data -> {

				NameDTO nameResults = retrieveCastName(data.getnConst(), data.gettConst(), isBaconSearch);
				data.setActorsName(null != nameResults ? nameResults.getPrimaryName() : null);
				data.setKnownForTitles(null != nameResults ? Arrays.asList(nameResults.getKnownForTitles()) : null);
				return data;
			}).collect(Collectors.toList());
		}

		return castCrewLst;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lunatech.imdb.helper.IMDbHelperUtil#retrieveRecordsFromRatingTsv()
	 */
	@Override
	public List<RatingsDTO> retrieveRecordsFromRatingTsv() {

		Resource resourceRatings = resourceLoader.getResource("classpath:dataset/ratingsdata.tsv");

		List<RatingsDTO> ratingsList = new ArrayList<RatingsDTO>();
		try {
			File file = resourceRatings.getFile();
			List<String> ratingsData = Files.readAllLines(file.toPath());

			try (Stream<String> stream = ratingsData.stream()) {

				ratingsList = stream.skip(1).map(line -> line.split(IMDbHelper.TAB)).map(data -> {
					RatingsDTO ratingsDTO = new RatingsDTO();

					for (int i = 0; i < data.length; i++) {
						switch (i) {
						case 0:
							ratingsDTO.settConst(data[i]);
							break;

						case 1:
							ratingsDTO.setAverageRating(data[i]);
							break;
						default:
							ratingsDTO.setNumVotes(data[i]);
							break;
						}
					}
					return ratingsDTO;
				}).collect(Collectors.toList());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ratingsList;
	}

	/* (non-Javadoc)
	 * @see com.lunatech.imdb.helper.IMDbUtilService#fetchMovieRecords(java.lang.Boolean)
	 */
	public List<MovieDTO> fetchMovieRecords(Boolean isBaconSearch) {
		List<MovieDTO> searchResult = new ArrayList<MovieDTO>();
		String fileName = isBaconSearch ? "classpath:dataset/moviedataforbacon.tsv" : "classpath:dataset/moviedata.tsv";

		Resource resourceRatings = resourceLoader.getResource(fileName);
		try {
			File file = resourceRatings.getFile();
			List<String> lines = null;
			lines = isBaconSearch ? Files.readAllLines(file.toPath()) :
			// Search in file line by line having the search keyword
					Files.lines(file.toPath()).filter(line -> line.contains(IMDbHelper.MOVIE)).collect(Collectors.toList());

			try (Stream<String> stream = lines.stream()) {
				// For Each Split by tab
				searchResult = stream.skip(1).map(line -> line.split(IMDbHelper.TAB)).map(data -> {
					MovieDTO titleBasicDTO = new MovieDTO();
					// Build Movie DTO
					buildMovieObjectWithParsedData(data, titleBasicDTO);

					return titleBasicDTO;
				}).collect(Collectors.toList());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return searchResult;
	}

}
