package com.searchpicto.ws.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
public class PictoControllerTest {

	/**
	 * Mock MVC.
	 */
	@Autowired
	private MockMvc mockMvc;

	/**
	 * Mock PictoService? 
	 */
	@MockBean
	private PictoServiceImpl pictoServiceMock;

	/**
	 * Method to init a Picto with Tags.
	 * 
	 * @param location The Media location.
	 * @param tags     The Picto Tags.
	 * @return The created Picto.
	 */
	private Picto initPicto(String location, Set<String> tags) {
		Picto picto = new Picto();
		picto.setMedia(new Media(location));
		picto.setTags(tags.stream().map(tag -> new Tag(tag)).collect(Collectors.toSet()));
		return picto;
	}

	/**
	 * Tests getPictoById method when a picto is found.
	 */
	@Test
	void getPictoById_existingPicto_found() throws Exception {
		Long id = Long.valueOf(2);
		Picto picto = initPicto("Parchemein.jpg",
				Stream.of("parchemin", "detail", "contrat", "legislation").collect(Collectors.toSet()));
		when(pictoServiceMock.getPictoById(id)).thenReturn(Optional.of(picto));

		this.mockMvc.perform(get("/picto?id=" + id)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(getJson(picto), false));
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
		Picto picto = initPicto("Parchemein.jpg",
				Stream.of("parchemin", "detail", "contrat", "legislation").collect(Collectors.toSet()));
		Set<Picto> pictos = new HashSet<>();
		pictos.add(picto);

		when(pictoServiceMock.findPictosByTagName("contrat")).thenReturn(pictos);
		this.mockMvc.perform(get("/pictos?tag=contrat")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(getJson(pictos), false));
	}

	/**
	 * Tests findPictosByTag method when pictos are found.
	 */
	@Test
	void findPictosByTag_existingPictos_found() throws Exception {
		Picto picto1 = initPicto("Parchemein.jpg",
				Stream.of("parchemin", "detail", "contrat", "legislation").collect(Collectors.toSet()));
		Picto picto2 = initPicto("Truc.jpg",
				Stream.of("encore", "allo", "contrat", "legislation").collect(Collectors.toSet()));
		Set<Picto> pictos = new HashSet<>();
		pictos.add(picto1);
		pictos.add(picto2);

		when(pictoServiceMock.findPictosByTagName("contrat")).thenReturn(pictos);
		this.mockMvc.perform(get("/pictos?tag=contrat")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(getJson(pictos), false));
	}

	/**
	 * Tests findPictosByTag method when no picto found.
	 */
	@Test
	void findPictosByTag_noPicto_404() throws Exception {
		Picto picto = initPicto("Parchemein.jpg",
				Stream.of("parchemin", "detail", "contrat", "legislation").collect(Collectors.toSet()));
		Set<Picto> pictos = new HashSet<>();
		pictos.add(picto);

		when(pictoServiceMock.findPictosByTagName("contrat")).thenReturn(new HashSet<>());
		this.mockMvc.perform(get("/pictos?tag=contrat")).andDo(print()).andExpect(status().isNotFound())
				.andExpect(content().string(containsString("Could not find picto for the tag : contrat")));
	}

	/**
	 * Generate JSON from an object.
	 * 
	 * @param object The object.
	 * @return The json generated.
	 * @throws JsonProcessingException The exception thrown when a parsing problem occurs.
	 */
	private String getJson(Object object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		return mapper.writeValueAsString(object);
	}
}
