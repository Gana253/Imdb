/**
 * 
 */
package com.java.imdb.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.imdb.dto.MovieDTO;
import com.java.imdb.dto.MovieNameWithRatings;
import com.java.imdb.service.IMDbSerivce;

/**
 * @author Ganapathy_N
 * 
 *         Controller class - to manage endpoint
 */
@RestController
@RequestMapping("/lunatech/imdb")
public class IMDbController {
	private final Logger log = LoggerFactory.getLogger(IMDbController.class);
	@Autowired
	private IMDbSerivce imdbService;

	/**
	 * Search Movie by Name - Use Case 1 Movie name passed through end point -
	 * whitespace should be replaced with tide symbol "~"
	 * 
	 * Example : Movie Name :Pauvre Pierrot To be passed as :Pauvre~Pierrot
	 * 
	 * @param movieName
	 * @return
	 */

	@GetMapping(path = "/searchMovie/{movieName}", produces = { MediaType.APPLICATION_JSON_VALUE })
	private ResponseEntity<?> searchMovie(@PathVariable String movieName) {
		log.info("Request Received for movie name search--{}", movieName);
		MovieDTO movie = imdbService.searchMovieByName(movieName);
		if (null != movie) {
			return new ResponseEntity<>(movie, HttpStatus.OK);
		} else {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Movie Search Error", "Couldnt find the movie name in database ");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers)
					.body("Error occured - Couldnt find the movie name in database ");
		}
	}

	/**
	 * Method to retrieve top rated movie based on genre
	 * 
	 * Example Genre - Adventure
	 * 
	 * @param genre
	 * @return
	 */
	@GetMapping(path = "/topMovieRatings/{genre}", produces = { MediaType.APPLICATION_JSON_VALUE })
	private ResponseEntity<?> topRatedMoviesByGenre(@PathVariable String genre) {

		log.info("Request Received for fetching top rated movies fo genre--{}", genre);
		List<MovieNameWithRatings> movieLst = imdbService.listTopRatedMoviesByGenre(genre);
		if (null != movieLst) {
			return new ResponseEntity<>(movieLst, HttpStatus.OK);
		} else {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Top Rated movies search Error", "Couldnt find movies for the genre");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers)
					.body("Error occured - Couldnt find movies for the genre");
		}
	}

	/**
	 * To retrieve the genres the person acted with the count of each genre movies.
	 * 
	 * @param name
	 * @return
	 */
	@GetMapping(path = "/typeCasting/{name}", produces = { MediaType.APPLICATION_JSON_VALUE })
	private ResponseEntity<?> typeCasting(@PathVariable String name) {
		log.info("Search for the genre movies the person typecaseted--{}", name);
		Map<String, Integer> result = imdbService.typeCastingActors(name);
		if (null != result) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Type Casting Error", "Error occured while fetching the typecasting done by the actor");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers)
					.body("Error occured while fetching the typecasting done by the actor");
		}
	}

	/**
	 * To fetch the movies in which the persons acted together
	 * 
	 * Example : Movie Id : tt0117057 First Person : Lauren Bacall
	 * 
	 * Second Person : Barbra Streisand if the user searches with the above
	 * mentioned persons , this api will return the movie details of movie id
	 * tt0117057
	 * 
	 * @param firstPerson
	 * @param secondPerson
	 * @return
	 */
	@GetMapping(path = "/coincidence/{firstPerson}/{secondPerson}", produces = { MediaType.APPLICATION_JSON_VALUE })
	private ResponseEntity<?> coincidence(@PathVariable String firstPerson, @PathVariable String secondPerson) {
		log.info("Retrieve the movies in which the persons acted together - 1 {} - 2 {}", firstPerson, secondPerson);
		List<MovieDTO> results = imdbService.coincidence(firstPerson, secondPerson);
		if (null != results) {
			return new ResponseEntity<>(results, HttpStatus.OK);
		} else {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Coincidence Error", "Error occured while fetching the movie which they have acted together");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers)
					.body("Error occured while fetching the movie which they have acted together");
		}
	}

	/**
	 * To fetch bacon number for the actor
	 * 
	 * @param actorName
	 * @return
	 */
	@GetMapping(path = "/findBaconNumber/{actorName}", produces = { MediaType.APPLICATION_JSON_VALUE })
	private ResponseEntity<?> findBaconNumber(@PathVariable String actorName) {
		log.info("Retrieve the movies in which the persons acted together - 1 {} - 2 {}", actorName);
		int results = imdbService.findBaconNumber(actorName);
		if (results != -1) {
			return new ResponseEntity<>(results, HttpStatus.OK);
		} else {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Find Bacon Number Error", "Couldnt Find Bacon Number for the actor");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers)
					.body("Couldnt Find Bacon Number for the actor");
		}
	}
}
