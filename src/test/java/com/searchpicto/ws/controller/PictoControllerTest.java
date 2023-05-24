package com.searchpicto.ws.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.searchpicto.ws.dto.PictoDto;
import com.searchpicto.ws.model.Media;
import com.searchpicto.ws.model.Picto;
import com.searchpicto.ws.model.Tag;
import com.searchpicto.ws.service.PictoServiceImpl;

/**
 * 
 * Test class for {@link PictoServiceImpl}.
 *
 * @author carol
 *
 */

@WebMvcTest(PictoController.class)
@ComponentScan("com.searchpicto.ws.mapper")
class PictoControllerTest {

	/**
	 * Mock MVC.
	 */
	@Autowired
	private MockMvc mockMvc;

	/**
	 * Mock PictoService.
	 */
	@MockBean
	private PictoServiceImpl pictoServiceMock;

	/**
	 * Method to init a Picto with Tags.
	 * 
	 * @param id       The Picto id.
	 * @param location The Media location.
	 * @param title    The Media title.
	 * @param tags     The Picto Tags.
	 * @return The created Picto.
	 */
	private Picto initPicto(Long id, String location, String title, Set<String> tags, LocalDateTime creationDate) {
		Picto picto = new Picto();
		picto.setPictoId(id);
		picto.setMedia(new Media(location, title));
		picto.setTags(tags.stream().map(Tag::new).collect(Collectors.toSet()));
		picto.setCreationDate(creationDate);
		return picto;
	}

	/**
	 * Tests getLastPictosAdded method when pictos are found.
	 */
	@Test
	void getLastPictosAdded_pictos_found() throws Exception {
		// Init values
		int sizeLimit = 10;
		Long id1 = Long.valueOf(2);
		Long id2 = Long.valueOf(10);
		String location1 = "Parchemin.jpg";
		String title1 = "Un parchemin";
		String title2 = "Un truc";
		String location2 = "Truc.jpg";
		var tagsNames1 = Stream.of("parchemin", "detail", "contrat", "legislation").collect(Collectors.toSet());
		var tagsNames2 = Stream.of("encore", "allo", "contrat", "legislation").collect(Collectors.toSet());

		var pictos = new ArrayList<Picto>();
		pictos.add(initPicto(id1, location1, title1, tagsNames1, LocalDateTime.of(1990, 06, 04, 10, 20)));
		pictos.add(initPicto(id2, location2, title2, tagsNames2, LocalDateTime.of(1987, 05, 18, 10, 20)));

		var pictosExpected = new ArrayList<PictoDto>();
		pictosExpected.add(new PictoDto(id1, location1, tagsNames1, "1990-06-04", title1));
		pictosExpected.add(new PictoDto(id2, location2, tagsNames2, "1987-05-18", title2));

		// Mock
		when(pictoServiceMock.getLastPictosAdded(sizeLimit)).thenReturn(pictos);

		// Test
		this.mockMvc.perform(get("/pictos?last=" + sizeLimit)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(getJson(pictosExpected), false));
	}

	/**
	 * Tests getLastPictosAdded method when no pictos are found.
	 */
	@Test
	void getLastPictosAdded_pictos_none() throws Exception {
		// Init values
		int sizeLimit = 10;
		var pictosExpected = new ArrayList<PictoDto>();

		// Mock
		when(pictoServiceMock.getLastPictosAdded(sizeLimit)).thenReturn(new ArrayList<Picto>());

		// Test
		this.mockMvc.perform(get("/pictos?last=" + sizeLimit)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(getJson(pictosExpected), false));
	}

	/**
	 * Tests getLastPictosAdded method when null a given by the service.
	 */
	@Test
	void getLastPictosAdded_pictos_null() throws Exception {
		// Init values
		int sizeLimit = 10;
		var pictosExpected = new ArrayList<PictoDto>();

		// Mock
		when(pictoServiceMock.getLastPictosAdded(sizeLimit)).thenReturn(null);

		// Test
		this.mockMvc.perform(get("/pictos?last=" + sizeLimit)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(getJson(pictosExpected), false));
	}

	/**
	 * Tests getPictoById method when a picto is found.
	 */
	@Test
	void getPictoById_existingPicto_found() throws Exception {
		// Init values
		Long id = Long.valueOf(2);
		String location = "Parchemin.jpg";
		String title = "Un parchemin";
		var tagsNames = Stream.of("parchemin", "detail", "contrat", "legislation").collect(Collectors.toSet());
		Picto picto = initPicto(id, location, title, tagsNames, LocalDateTime.of(1990, 06, 04, 10, 20));
		PictoDto pictoExcepted = new PictoDto(id, location, tagsNames, "1990-06-04", title);

		// Mock
		when(pictoServiceMock.getPictoById(id)).thenReturn(Optional.of(picto));

		// Test
		this.mockMvc.perform(get("/picto?id=" + id)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(getJson(pictoExcepted), false));
	}

	/**
	 * Tests getPictoById method when no picto is found.
	 */
	@Test
	void getPictoById_noPicto_404() throws Exception {
		Long id = Long.valueOf(2);
		when(pictoServiceMock.getPictoById(id)).thenReturn(Optional.empty());

		this.mockMvc.perform(get("/picto?id=" + id)).andDo(print()).andExpect(status().isNotFound())
				.andExpect(content().string(containsString("Could not find picto with id : " + id)));
	}

	/**
	 * Tests findPictosByTag method when one picto is found.
	 */
	@Test
	void findPictosByTag_existingOnePicto_found() throws Exception {
		// Init values
		Long id = Long.valueOf(2);
		String location = "Parchemin.jpg";
		String title = "Un parchemin";
		var tagsNames = Stream.of("parchemin", "detail", "contrat", "legislation").collect(Collectors.toSet());
		var pictos = new HashSet<Picto>();
		pictos.add(initPicto(id, location, title, tagsNames, LocalDateTime.of(1990, 06, 04, 10, 20)));

		var pictosExpected = new HashSet<PictoDto>();
		pictosExpected.add(new PictoDto(id, location, tagsNames, "1990-06-04", title));

		// Mock
		when(pictoServiceMock.findPictosByTagName("contrat")).thenReturn(pictos);

		// Test
		this.mockMvc.perform(get("/pictos?tag=contrat")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(getJson(pictosExpected), false));
	}

	/**
	 * Tests findPictosByTag method when pictos are found.
	 */
	@Test
	void findPictosByTag_existingPictos_found() throws Exception {
		// Init values
		Long id1 = Long.valueOf(2);
		Long id2 = Long.valueOf(10);
		String location1 = "Parchemin.jpg";
		String title1 = "Un parchemin";
		String title2 = "Un truc";
		String location2 = "Truc.jpg";
		var tagsNames1 = Stream.of("parchemin", "detail", "contrat", "legislation").collect(Collectors.toSet());
		var tagsNames2 = Stream.of("encore", "allo", "contrat", "legislation").collect(Collectors.toSet());

		var pictos = new HashSet<Picto>();
		pictos.add(initPicto(id1, location1, title1, tagsNames1, LocalDateTime.of(1990, 06, 04, 10, 20)));
		pictos.add(initPicto(id2, location2, title2, tagsNames2, LocalDateTime.of(1987, 05, 18, 10, 20)));

		var pictosExpected = new HashSet<PictoDto>();
		pictosExpected.add(new PictoDto(id1, location1, tagsNames1, "1990-06-04", title1));
		pictosExpected.add(new PictoDto(id2, location2, tagsNames2, "1987-05-18", title2));

		// Mock
		when(pictoServiceMock.findPictosByTagName("contrat")).thenReturn(pictos);

		// Test
		this.mockMvc.perform(get("/pictos?tag=contrat")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(getJson(pictosExpected), false));
	}

	/**
	 * Tests findPictosByTag method when no picto found.
	 */
	@Test
	void findPictosByTag_noPicto_empty() throws Exception {
		Picto picto = initPicto(Long.valueOf(20), "Parchemein.jpg", "Un parchemin",
				Stream.of("parchemin", "detail", "contrat", "legislation").collect(Collectors.toSet()),
				LocalDateTime.now());
		var pictos = new HashSet<Picto>();
		pictos.add(picto);

		when(pictoServiceMock.findPictosByTagName("contrat")).thenReturn(new HashSet<>());
		this.mockMvc.perform(get("/pictos?tag=contrat")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(getJson(new HashSet<>()), false));
	}

	/**
	 * Generate JSON from an object.
	 * 
	 * @param object The object.
	 * @return The json generated.
	 * @throws JsonProcessingException The exception thrown when a parsing problem
	 *                                 occurs.
	 */
	private String getJson(Object object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		return mapper.writeValueAsString(object);
	}
}
