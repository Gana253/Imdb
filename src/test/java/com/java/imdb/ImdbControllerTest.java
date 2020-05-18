/**
 * 
 */
package com.java.imdb;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.java.imdb.controller.IMDbController;

/**
 * @author Ganapathy_N
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ImdbApplication.class)
public class ImdbControllerTest {

	private static final Logger log = LoggerFactory.getLogger(ImdbControllerTest.class);

	private final String mockTypeCasting = "{\"Action\":1,\"Sci-Fi\":1,\"Drama\":3,\"Horror\":1,\"Music\":1,\"Crime\":1,\"Romance\":1,\"Mystery\":1}";

	private MockMvc webReqMockMvc;

	@Autowired
	private IMDbController iMDbController;
	public final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private final String mockMovieSearchResult = "{\"tConst\":\"tt0361127\",\"titleType\":\"movie\",\"primaryTitle\":\"The Woodsman\""
			+ ",\"originalTitle\":\"The Woodsman\",\"isAdult\":false,\"startYear\":\"2004\",\"endYear\":\"NA\",\"runtime\""
			+ ":\"87\",\"genres\":[\"Drama\"],\"cast_crew\":{\"Kyra Sedgwick\":\"actress\",\"Lee Daniels\":\""
			+ "producer\",\"Steven Fechter\":\"writer\",\"Kevin Bacon\":\"actor\",\"Nicole Kassell\":\"director\",\"Yasiin Bey\":\"actor\"}}";

	private final String mockCoincidenceResult = "[{\"tConst\":\"tt0117057\",\"titleType\":\"movie\",\"primaryTitle\":\""
			+ "The Mirror Has Two Faces\",\"originalTitle\":\"The Mirror Has Two Faces\",\"isAdult\""
			+ ":false,\"startYear\":\"1996\",\"endYear\":\"NA\",\"runtime\":\"126\",\"genres\":[\"Comedy\""
			+ ",\"Drama\",\"Romance\"],\"cast_crew\":null}]";

	@PostConstruct
	public void setup() {

		MockitoAnnotations.initMocks(this);
		this.webReqMockMvc = MockMvcBuilders.standaloneSetup(iMDbController).build();
	}

	/**
	 * Search By Movie Name - Status should be OK(200) and content to match the
	 * mockString
	 * 
	 * @throws Exception
	 */
	@Test
	public void searchByMovieName() throws Exception {

		webReqMockMvc.perform(get("/lunatech/imdb/searchMovie/" + "The~Woodsman").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().json(mockMovieSearchResult));
	}

	/**
	 * 
	 * Search By Movie Name - Searched movie not foind in database, Status
	 * Not Found(204)
	 * 
	 * @throws Exception
	 */
	@Test
	public void searchByMovieNameNotFound() throws Exception {

		webReqMockMvc.perform(get("/lunatech/imdb/searchMovie/" + "NotFound").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound());

	}

	/**
	 * Search By Genre- > Return List of Movie which above average for the genre,
	 * Status isOk(200)
	 * 
	 * @throws Exception
	 */
	@Test
	public void searchTopRatedMoviesByGenre() throws Exception {

		String response = webReqMockMvc
				.perform(get("/lunatech/imdb/topMovieRatings/" + "Horror").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		log.warn("Searched Result for genre->{}", response);

	}

	/**
	 * Search By Genre - Case where there is no movies matching genre - status
	 * not found
	 * 
	 * @throws Exception
	 */
	@Test
	public void searchTopRatedMoviesByGenreNotFound() throws Exception {

		webReqMockMvc.perform(get("/lunatech/imdb/topMovieRatings/" + "NotFound").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound());

	}

	/**
	 * Type Casting - Search for the movies acted by the person and populate the
	 * genres count that he acted, Status - isOk
	 * 
	 * @throws Exception
	 */
	@Test
	public void searchTypeCastingByName() throws Exception {

		webReqMockMvc.perform(get("/lunatech/imdb/typeCasting/" + "Kevin~Bacon").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().json(mockTypeCasting));

	}

	/**
	 * No Type Casting results fetched for the person - Staus - nnot found
	 * 
	 * @throws Exception
	 */
	@Test
	public void searchTypeCastingByNameNotFound() throws Exception {

		webReqMockMvc.perform(get("/lunatech/imdb/typeCasting/" + "NotFound").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound());

	}

	/**
	 * Search for the movies that two actors acted together- Status isOk
	 * 
	 * @throws Exception
	 */
	@Test
	public void searchCoincidenceOfTwoPerson() throws Exception {

		String response = webReqMockMvc
				.perform(get("/lunatech/imdb/coincidence/" + "Lauren~Bacall" + "/" + "Barbra~Streisand")
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().json(mockCoincidenceResult)).andReturn().getResponse()
				.getContentAsString();

		log.warn("Coinicidence Response ---{}", response);

	}

	/**
	 * No Coincidence found between two person
	 * 
	 * @throws Exception
	 */
	@Test
	public void searchCoincidenceOfTwoPersonNotFound() throws Exception {

		webReqMockMvc.perform(
				get("/lunatech/imdb/coincidence/" + "NotFound" + "/" + "NotFound1").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound());

	}

	/**
	 * value same as kevin bacon, test should return 0
	 * 
	 * @throws Exception
	 */
	@Test
	public void findBaconNumber_0() throws Exception {

		webReqMockMvc.perform(get("/lunatech/imdb/findBaconNumber/" + "Kevin~Bacon").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().json(String.valueOf(0)));

	}

	/**
	 * Person acted with Kevin Bacon Together- expected result 1
	 * 
	 * @throws Exception
	 */
	@Test
	public void findBaconNumber_1() throws Exception {

		webReqMockMvc.perform(get("/lunatech/imdb/findBaconNumber/" + "Yasiin~Bey").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().json(String.valueOf(1)));

	}

	/**
	 * Person acted with the actor who shared the screen with kevin bacon - expected
	 * result -2
	 * 
	 * @throws Exception
	 */
	@Test
	public void findBaconNumber_2() throws Exception {

		webReqMockMvc
				.perform(get("/lunatech/imdb/findBaconNumber/" + "Greta~Scacchi").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().json(String.valueOf(2)));

	}

	/**
	 * Person didnt acted with kevin bacon but the co artist of the person shared
	 * with Kevin Bacon- Expected result -3
	 * 
	 * Searched Person acted with-> Person A Acted with -> Person B Acted with ->
	 * Kevin Bacon : Degree 3
	 * 
	 * @throws Exception
	 */
	@Test
	public void findBaconNumber_3() throws Exception {

		webReqMockMvc.perform(get("/lunatech/imdb/findBaconNumber/" + "Elena~Cotta").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().json(String.valueOf(3)));

	}

	/**
	 * No relationship found between the actor and kevin bacon.
	 * 
	 * @throws Exception
	 */
	@Test
	public void findBaconNumber_NotFound() throws Exception {

		webReqMockMvc.perform(get("/lunatech/imdb/findBaconNumber/" + "Ajith~Kumar").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound());

	}
}
