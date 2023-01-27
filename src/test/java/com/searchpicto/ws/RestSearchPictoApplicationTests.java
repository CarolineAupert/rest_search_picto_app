package com.searchpicto.ws;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.searchpicto.ws.model.Media;
import com.searchpicto.ws.model.Picto;
import com.searchpicto.ws.model.Tag;
import com.searchpicto.ws.service.PictoService;

/**
 * Integration Test.
 * 
 * @author carol
 *
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RestSearchPictoApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private PictoService pictoService;
	
	/**
	 * Method to init a Picto with Tags.
	 * 
	 * @param location The Media location.
	 * @param tags     The Picto Tags.
	 * @return The created Picto.
	 */
	private Picto initPicto( String location, Set<String> tags) {
		Picto picto = new Picto();
		picto.setMedia(new Media(location));
		picto.setTags(tags.stream().map(tag -> new Tag(tag)).collect(Collectors.toSet()));
		pictoService.addNewPicto(picto);
		return picto;
	}	
	
	/**
	 * Init database for tests
	 * @throws JsonProcessingException 
	 */
	@BeforeAll
	void initData() throws JsonProcessingException {
		initPicto("Parchemin.jpg",
				Stream.of("parchemin", "detail", "contrat", "legislation").collect(Collectors.toSet()));
		initPicto("Truc.jpg",
				Stream.of("encore", "allo", "contrat", "legislation").collect(Collectors.toSet()));
	}

	/**
	 * Tests getPictoById method when a picto is found.
	 */
	@Test
	void getPictoById_existingPicto_found() throws Exception {
		this.mockMvc.perform(get("/picto?id=2")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Truc.jpg")));
	}

	/**
	 * Tests getPictoById method when no picto is found.
	 */
	@Test
	void getPictoById_noPicto_404() throws Exception {
		Long id = Long.valueOf(10);

		this.mockMvc.perform(get("/picto?id=" + id)).andDo(print()).andExpect(status().isNotFound())
				.andExpect(content().string(containsString("Could not find picto with id : " + id)));
	}

	/**
	 * Tests findPictosByTag method when one picto is found.
	 */
	@Test
	void findPictosByTag_existingOnePicto_found() throws Exception {
		this.mockMvc.perform(get("/pictos?tag=parchemin")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Parchemin.jpg")));
	}

	/**
	 * Tests findPictosByTag method when pictos are found.
	 */
	@Test
	void findPictosByTag_existingPictos_found() throws Exception {

		this.mockMvc.perform(get("/pictos?tag=contrat")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Parchemin.jpg"))).andExpect(content().string(containsString("Truc.jpg")));
	}

	/**
	 * Tests findPictosByTag method when no picto found.
	 */
	@Test
	void findPictosByTag_noPicto_404() throws Exception {

		this.mockMvc.perform(get("/pictos?tag=truc")).andDo(print()).andExpect(status().isNotFound())
				.andExpect(content().string(containsString("Could not find picto for the tag : truc")));
	}

}
