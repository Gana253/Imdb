/**
 * 
 */
package com.java.imdb.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.imdb.dto.ActorsDTO;
import com.java.imdb.dto.CastNCrewDTO;
import com.java.imdb.dto.MovieDTO;
import com.java.imdb.dto.MovieNameWithRatings;
import com.java.imdb.dto.NameDTO;
import com.java.imdb.dto.RatingsDTO;
import com.java.imdb.helper.IMDbUtilService;
import com.java.imdb.helper.impl.IMDbHelper;
import com.java.imdb.service.IMDbSerivce;

/**
 * @author Ganapathy_N
 *
 */
@Service("imdbService")
public class IMDbServiceImpl implements IMDbSerivce {

	private static final String KEVIN_BACON = "Kevin Bacon";

	private static final Logger log = LoggerFactory.getLogger(IMDbSerivce.class);

	private List<RatingsDTO> ratingsList = new ArrayList<RatingsDTO>();

	private List<MovieDTO> movieLstForBacon = new ArrayList<MovieDTO>();

	private List<MovieDTO> movieLst = new ArrayList<MovieDTO>();

	@Autowired
	private IMDbUtilService imdbUtilService;

	private Map<String, List<ActorsDTO>> graph;

	Set<String> keySet;

	@PostConstruct
	public void init() {
		ratingsList = imdbUtilService.retrieveRecordsFromRatingTsv();

		movieLst = imdbUtilService.fetchMovieRecords(false);
		/*
		 * Data Set up for Six degrees of Bacon -> Fectch the movielist from sample tsv
		 * file
		 */
		movieLstForBacon = imdbUtilService.fetchMovieRecords(true);
		// Based on movie list build graph
		buildGaph();
	}

	/**
	 * 1. Iterate the movielist for bacon and retrieve the crew members. 2. Create
	 * Graph based on the crew 3. Create Actor Node for each actor
	 * 
	 */
	public void buildGaph() {
		// Retrieve Crew Details of the movie
		Map<String, List<CastNCrewDTO>> crewMapForEachMovie = new HashMap<String, List<CastNCrewDTO>>();
		crewMapForEachMovie = movieLstForBacon.stream().collect(Collectors.toMap(MovieDTO::gettConst,
				movie -> imdbUtilService.retrieveCastNCrewForMovie(movie.gettConst(), true)));
		// Initialize Graph
		graph = new HashMap<String, List<ActorsDTO>>();
		// keyset to hold all the actors
		keySet = new HashSet<String>();
		// Create Map having key as movie and value as list of actor name
		Map<String, List<String>> result = crewMapForEachMovie.entrySet().stream() // Stream over entry set
				.collect(Collectors.toMap( // Collect final result map
						Map.Entry::getKey, // Key mapping is the same
						e -> e.getValue().stream() // Stream over list
								.map(CastNCrewDTO::getActorsName) // Apply mapping to MyObject
								.collect(Collectors.toList())) // Collect mapping into list
				);

		/*
		 * Create a graph - For Each Actor create the node
		 */
		result.entrySet().stream().forEach(e -> {
			String movie = e.getKey(); // Movie Name
			keySet.addAll(e.getValue()); // Add all the actor to the keyset
			addActors(movie);
			keySet.clear();
		});

	}

	/**
	 * Adding the actors to graph
	 * 
	 * @param movie
	 */
	private void addActors(String movie) {
		keySet.stream().forEach((key) -> {// Iterate the Actors
			String actorMain = key;
			// Check whether the actor is already added to graph if not add it to graph
			if (null != actorMain && !graph.containsKey(actorMain)) {
				List<ActorsDTO> newList = new ArrayList<ActorsDTO>();
				keySet.forEach(actors -> {
					if (actors != null && !actors.equals(actorMain)) {
						ActorsDTO node = new ActorsDTO(actors);
						node.getMovies().add(movie);// Add the movie to the list
						newList.add(node);
					}
				});
				graph.put(actorMain, newList); // Add to the graph
			}
			// If actor key already present retreivev the value and insert the movie
			if (null != actorMain && graph.get(actorMain) != null) {
				keySet.forEach(actors -> {
					if (actors != null && !actors.equals(actorMain)) {
						Boolean contains = false;
						List<ActorsDTO> list = graph.get(actorMain);
						if (!list.isEmpty()) {
							for (ActorsDTO a : list) {
								if (a.getName().equals(actors)) {
									a.getMovies().add(movie);
									contains = true;
									break;
								}
							}
							if (!contains) {
								ActorsDTO newNode = new ActorsDTO(actors);
								newNode.getMovies().add(movie);
								graph.get(actorMain).add(newNode);
							}
						}
					}
				});
			}

		});
	}

	/*
	 * private String[] headers(String path) throws IOException {
	 * 
	 * try (BufferedReader br = new BufferedReader(new FileReader(path))) { return
	 * br.readLine().split("\\t"); } }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lunatech.imdb.service.impl.IMDbSerivce#searchMovieByName(java.lang.
	 * String)
	 */
	public MovieDTO searchMovieByName(String movieName) {
		log.info("Start process to retrieve movie details");

		// Original Name with which user searched
		String orgMovieName = movieName.replace(IMDbHelper.TIDE_CONSTANT, IMDbHelper.SPACE);

		// Get the movie matching the user searched movie name
		MovieDTO movie = movieLst.stream()
				.filter(x -> x.getPrimaryTitle().equals(orgMovieName) || x.getOriginalTitle().equals(orgMovieName))
				.findAny().orElse(null);
		if (null == movie) {
			return null; // Return movie not found
		}
		// Retrieve cast and crew members for the movie
		List<CastNCrewDTO> castCrewLst = imdbUtilService.retrieveCastNCrewForMovie(movie.gettConst(), false);

		Map<String, String> castMap = castCrewLst.stream().filter(castCrew -> null != castCrew.getActorsName())
				.collect(Collectors.toMap(CastNCrewDTO::getActorsName, CastNCrewDTO::getCategory));
		// Add the crew details
		movie.setCastNCrew(castMap);

		log.info("End of the process to retrieve movie-{}" + movie.toString());

		return movie;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lunatech.imdb.service.impl.IMDbSerivce#listTopRatedMoviesByGenre(java.
	 * lang.String)
	 */
	public List<MovieNameWithRatings> listTopRatedMoviesByGenre(String genre) {
		log.info("Retrieve top rated movies -");

		// Filter the result based on the input genre and title movie
		List<MovieDTO> movieRelatedToSearchedGenre = movieLst.stream().filter(r -> r.getGenres().contains(genre))
				.collect(Collectors.toList());

		if (null == movieRelatedToSearchedGenre) {
			return null;
		}
		// Find the average rating out of all the records in the rating tsv file to
		// display the above average movies
		Double averageRating = ratingsList.stream()
				.collect(Collectors.averagingDouble(p -> Double.parseDouble(p.getAverageRating())));

		// List of movies having rating above average
		List<RatingsDTO> movieAboveAverageRating = ratingsList.stream()
				.filter(r -> Double.parseDouble(r.getAverageRating()) > averageRating).collect(Collectors.toList());
		// Index the movieAboveAverage raing list
		Map<String, RatingsDTO> movieAboveAverageMap = movieAboveAverageRating.stream()
				.collect(Collectors.toMap(RatingsDTO::gettConst, Function.identity()));
		/*
		 * Compare two objects and fetch the movie details 1.
		 * movieRelatedToSearchedGenre 2. movieAboveAverageMap
		 */
		List<MovieNameWithRatings> movieLst = movieRelatedToSearchedGenre.stream()
				.filter(data -> movieAboveAverageMap.containsKey(data.gettConst())).map(data -> {
					RatingsDTO rating = movieAboveAverageMap.get(data.gettConst());
					return new MovieNameWithRatings(data.gettConst(), Double.parseDouble(rating.getAverageRating()),
							data.getPrimaryTitle(), data.getOriginalTitle());

				}).collect(Collectors.toList());

		if (movieLst.isEmpty()) {
			return null;
		}
		// Sort the movies based on the rating(Top Rated to Low)
		movieLst.sort(Comparator.comparing(MovieNameWithRatings::getAverageRating).reversed());
		log.info("End Retrieve top rated movies for genre-{}", genre);

		return movieLst;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lunatech.imdb.service.impl.IMDbSerivce#typeCastingActors(java.lang.
	 * String)
	 */
	public Map<String, Integer> typeCastingActors(String name) {
		log.info("Search for the movies acted by the person in each genre-");
		NameDTO nameResults = null;

		String orgActorName = name.replace(IMDbHelper.TIDE_CONSTANT, IMDbHelper.SPACE);

		// Retrieve List of Name objects matching the search criteria
		nameResults = imdbUtilService.retrieveCastName(orgActorName, null, false);

		if (null == nameResults) {
			return null;
		}
		// Get the list of movies the person acted
		List<String> movieId = Stream.of(nameResults.getKnownForTitles()).map(elem -> new String(elem))
				.collect(Collectors.toList());

		List<MovieDTO> result = new ArrayList<MovieDTO>();
		// For each movie - fetch the movie details
		result = movieId.stream().map(data -> {
			return !data.equals("\\N") ? imdbUtilService.fetchRecordsFromBaseTitle(data).get(0) : null;

		}).collect(Collectors.toList());
		result.removeAll(Collections.singleton(null));

		/*
		 * Fetch the genres that the person acted from the movie list array list will
		 * hold the duplicate values
		 */
		List<String> genres = result.stream().flatMap(e -> e.getGenres().stream()).collect(Collectors.toList());

		if (genres.isEmpty()) {
			return null;
		}
		// Count the occurence of each genre in the list
		Map<String, Integer> resultAvg = IMDbHelper.countFrequencies(genres);

		return resultAvg;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lunatech.imdb.service.impl.IMDbSerivce#coincidence(java.lang.String,
	 * java.lang.String)
	 */
	public List<MovieDTO> coincidence(String firstName, String secondName) {
		log.info("Retrieve the movies which they have acted together");
		NameDTO firstNameResults = null;

		NameDTO secondNameResults = null;
		// Fetch the orignal name of both persons
		String orgFirstActorName = firstName.replace(IMDbHelper.TIDE_CONSTANT, IMDbHelper.SPACE);

		String orgSecondActorName = secondName.replace(IMDbHelper.TIDE_CONSTANT, IMDbHelper.SPACE);

		// keyWordToSearch = IMDbHelper.retrieveKeyWordToSearch(firstName);
		// Retrieve the name results of first person
		firstNameResults = imdbUtilService.retrieveCastName(orgFirstActorName, null, false);

		// keyWordToSearch = IMDbHelper.retrieveKeyWordToSearch(secondName);
		// Retrieve the name results of second person
		secondNameResults = imdbUtilService.retrieveCastName(orgSecondActorName, null, false);

		if (null == firstNameResults || null == secondNameResults) {
			return null;
		}
		// Get the movie list of the first person
		List<String> movieIdFirstActor = Stream.of(firstNameResults.getKnownForTitles()).map(elem -> new String(elem))
				.collect(Collectors.toList());
		// Get the movie list of the second person
		List<String> movieIdSecondActor = Stream.of(secondNameResults.getKnownForTitles()).map(elem -> new String(elem))
				.collect(Collectors.toList());
		// Retrieve the common movies they have accted
		List<String> commonMovieId = movieIdFirstActor.stream().filter(data -> movieIdSecondActor.contains(data))
				.map(data -> {
					return data;
				}).collect(Collectors.toList());

		List<MovieDTO> result = new ArrayList<MovieDTO>();
		// Retrieve the movie details of the common movie they acted
		result = commonMovieId.stream().map(data -> {
			return imdbUtilService.fetchRecordsFromBaseTitle(data).get(0);

		}).collect(Collectors.toList());

		log.info("End- Retrieve the movies which they have acted together");
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lunatech.imdb.service.IMDbSerivce#findBaconNumber(java.lang.String)
	 */
	public int findBaconNumber(String inputActorName) {
		// Original Name with which user searched
		String actor = inputActorName.replace(IMDbHelper.TIDE_CONSTANT, IMDbHelper.SPACE);
		// If searched actor name equals kevin bacon return 0
		if (actor.equals(KEVIN_BACON)) {
			return 0;
		}
		// If searched actor name is not available in graph
		if (!graph.containsKey(actor) || actor == null) {
			return -1;
		}

		Queue<ActorsDTO> queue = new LinkedList<ActorsDTO>();

		ActorsDTO searchedActor = new ActorsDTO(actor);
		searchedActor.setDistance(0);
		queue.add(searchedActor);
		Set<String> visited = new HashSet<String>();
		visited.add(actor); // Add the searched actor as visited node
		while (!queue.isEmpty()) {
			ActorsDTO last = queue.remove(); // Retriee the actor
			String actorName = last.getName();
			if (actorName.equals(KEVIN_BACON)) {
				return last.getDistance();
			}
			for (ActorsDTO node : graph.get(actorName)) {
				node.setDistance(last.getDistance() + 1);
				node.setPrev(last);
				if (!visited.contains(node.getName())) {
					queue.add(node);
					visited.add(node.getName());
				}
			}
		}
		return -1;
	}

}
