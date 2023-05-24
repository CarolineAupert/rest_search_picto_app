/**
 * 
 */
package com.searchpicto.ws.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.searchpicto.ws.model.Media;
import com.searchpicto.ws.model.Picto;
import com.searchpicto.ws.model.Tag;
import com.searchpicto.ws.repository.PictoRepository;
import com.searchpicto.ws.repository.TagRepository;

/**
 * 
 * Test class for {@link PictoServiceImpl}.
 *
 * @author carol
 *
 */

@ExtendWith(MockitoExtension.class)
class PictoServiceTest {

	/**
	 * The mock Tag Repository.
	 */
	@Mock
	private TagRepository tagRepositoryMock;

	/**
	 * The mock Picto Repository.
	 */
	@Mock
	private PictoRepository pictoRepositoryMock;

	/**
	 * The service to test.
	 */
	@InjectMocks
	private PictoService pictoService = new PictoServiceImpl();

	/**
	 * Method to init a Picto with Tags.
	 * 
	 * @param location The Media location.
	 * @param title    The Media title.
	 * @param tags     The Picto Tags.
	 * @return The created Picto.
	 */
	private Picto initPicto(String location, String title, Set<String> tags) {
		Picto picto = new Picto();
		picto.setCreationDate(LocalDateTime.now());
		picto.setMedia(new Media(location, title));
		picto.setTags(tags.stream().map(Tag::new).collect(Collectors.toSet()));
		return picto;
	}

	/**
	 * Tests the method getLastPictosAdded with 4 first pictos.
	 */
	@Test
	void getLastPictosAdded_pictoReturned_all() {
		// Initiating data
		int pictoResultsLimit = 4;
		Picto picto1 = initPicto("Loupe.jpg", "Une loupe",
				Stream.of("loupe", "détail", "chercher", "analyser", "zoom").collect(Collectors.toSet()));
		Picto picto2 = initPicto("Parchemin.jpg", "Un parchemin",
				Stream.of("parchemin", "détail", "contrat", "législation").collect(Collectors.toSet()));
		Picto picto3 = initPicto("Truc.jpg", "Un parchemin",
				Stream.of("parchemin", "détail", "contrat", "législation").collect(Collectors.toSet()));
		Picto picto4 = initPicto("Perso.jpg", "Un parchemin",
				Stream.of("parchemin", "détail", "contrat", "législation").collect(Collectors.toSet()));
		List<Picto> pictos = Stream.of(picto1, picto2, picto3, picto4).collect(Collectors.toList());
		PageImpl<Picto> pictosPage = new PageImpl<>(pictos);

		Mockito.when(pictoRepositoryMock
				.findAll(PageRequest.of(0, pictoResultsLimit, Sort.by(Sort.Direction.DESC, "creationDate"))))
				.thenReturn(pictosPage);
		assertEquals(pictos, pictoService.getLastPictosAdded(pictoResultsLimit),
				"The pictos returned are not the same.");

	}

	/**
	 * Tests the method getLastPictosAdded with no pictos found.
	 */
	@Test
	void getLastPictosAdded_pictoReturned_none() {
		// Initiating data
		int pictoResultsLimit = 10;
		PageImpl<Picto> pictosPage = null;

		Mockito.when(pictoRepositoryMock
				.findAll(PageRequest.of(0, pictoResultsLimit, Sort.by(Sort.Direction.DESC, "creationDate"))))
				.thenReturn(pictosPage);
		assertEquals(new ArrayList<>(), pictoService.getLastPictosAdded(pictoResultsLimit),
				"The pictos returned are not the same.");

	}

	/**
	 * Tests the method getLastPictosAdded when size wanted is 0.
	 */
	@Test
	void getLastPictosAdded_pictoLimit0_none() {
		// Initiating data
		int pictoResultsLimit = 0;

		assertEquals(new ArrayList<>(), pictoService.getLastPictosAdded(pictoResultsLimit),
				"The pictos returned are not the same.");

		verify(pictoRepositoryMock, never()).findAll(any(Pageable.class));

	}

	/**
	 * Tests the method getPictoById with a working Picto.
	 */
	@Test
	void getPictoById_pictoReturned_equals() {
		Long id = Long.valueOf(2);
		Picto picto = initPicto("Parchemein.jpg", "Un parchemin",
				Stream.of("parchemin", "détail", "contrat", "législation").collect(Collectors.toSet()));

		Mockito.when(pictoRepositoryMock.findById(id)).thenReturn(Optional.of(picto));
		assertEquals(Optional.of(picto), pictoService.getPictoById(id), "The Picto found is not the same.");
	}

	/**
	 * Tests the method getPictoById with null Picto id.
	 */
	@Test
	void getPictoById_idNull_empty() {
		assertTrue(pictoService.getPictoById(null).isEmpty(), "The Picto should be empty since the id is null.");
	}

	/**
	 * Tests the method getPictoById with non existing Picto.
	 */
	@Test
	void getPictoById_pictoNotFound_empty() {
		Long id = Long.valueOf(2);
		Mockito.when(pictoRepositoryMock.findById(id)).thenReturn(Optional.empty());
		assertTrue(pictoService.getPictoById(id).isEmpty(), "The Picto should be empty since no picto was found.");
	}

	/**
	 * Tests the method findPictosByTagName with a null Tag.
	 */
	@Test
	void findPictosByTagName_nullTag_empty() {
		String tagName = null;
		assertTrue(pictoService.findPictosByTagName(tagName).isEmpty(),
				"No pictos should be found since the tag is null.");
	}

	/**
	 * Tests the method findPictosByTagName with an empty Tag.
	 */
	@Test
	void findPictosByTagName_emptyTag_empty() {
		String tagName = "";
		assertTrue(pictoService.findPictosByTagName(tagName).isEmpty(),
				"No pictos should be found since the tag is empty.");
	}

	/**
	 * Tests the method findPictosByTagName with a null Tag.
	 */
	@Test
	void findPictosByTagName_blankTag_empty() {
		String tagName = " ";
		assertTrue(pictoService.findPictosByTagName(tagName).isEmpty(),
				"No pictos should be found since the tag is blank.");
	}

	/**
	 * Tests the method findPictosByTagName with an unknown Tag.
	 */
	@Test
	void findPictosByTagName_unknownTag_empty() {
		String tagName = "unknown";
		Mockito.when(tagRepositoryMock.findById(tagName)).thenReturn(Optional.empty());
		assertTrue(pictoService.findPictosByTagName(tagName).isEmpty(),
				"No pictos should be found since the tag is not associated to pictos.");
	}

	/**
	 * Tests the method findPictosByTagName with a known Tag.
	 */
	@Test
	void findPictosByTagName_knownTag_equals() {
		// Initiating data
		String tagName = "known";
		Picto picto1 = initPicto("Loupe.jpg", "Une loupe",
				Stream.of("loupe", "détail", "chercher", "analyser", "zoom").collect(Collectors.toSet()));
		Picto picto2 = initPicto("Parchemein.jpg", "Un parchemin",
				Stream.of("parchemin", "détail", "contrat", "législation").collect(Collectors.toSet()));
		Set<Picto> pictos = Stream.of(picto1, picto2).collect(Collectors.toSet());
		Tag foundTag = new Tag();
		foundTag.setPictos(pictos);

		// Testing
		Mockito.when(tagRepositoryMock.findById(tagName)).thenReturn(Optional.of(foundTag));
		assertEquals(pictos, pictoService.findPictosByTagName(tagName), "Pictos should match.");
	}

	/**
	 * Tests the method addNewPicto with a null Picto.
	 */
	@Test
	void addNewPicto_pictoNull_nothing() {
		assertDoesNotThrow(() -> pictoService.addNewPicto(null));
	}

	/**
	 * Tests the method addNewPicto with a Picto with null tags.
	 */
	@Test
	void addNewPicto_pictoTagNull_nothing() {
		Picto picto = new Picto();
		picto.setTags(null);
		assertDoesNotThrow(() -> pictoService.addNewPicto(picto));
	}

	/**
	 * Tests the method addNewPicto with a Picto with no tags.
	 */
	@Test
	void addNewPicto_pictoNoTags_save() {
		Picto picto = new Picto();
		picto.setTags(new HashSet<>());
		pictoService.addNewPicto(picto);

		verify(tagRepositoryMock, never()).save(any());
		verify(pictoRepositoryMock).save(picto);
	}

	/**
	 * Tests the method addNewPicto with a Picto with tags.
	 */
	@Test
	void addNewPicto_pictoWithTags_save() {
		Picto picto = initPicto("Loupe.jpg", "Une loupe",
				Stream.of("loupe", "détail", "chercher", "analyser", "zoom").collect(Collectors.toSet()));
		pictoService.addNewPicto(picto);

		verify(tagRepositoryMock, times(5)).save(any());
		verify(pictoRepositoryMock).save(picto);

	}

	/**
	 * Tests the method addPictoTags with a null Picto.
	 */
	@Test
	void addPictoTags_nullPicto_nothing() {
		assertEquals(null, pictoService.addPictoTags(null, new HashSet<>()),
				"The PIcto returned should be equal to the one given");
	}

	/**
	 * Tests the method addPictoTags with null Tags.
	 */
	@Test
	void addPictoTags_nullTags_nothing() {
		Picto picto = new Picto();
		assertEquals(picto, pictoService.addPictoTags(picto, null),
				"The PIcto returned should be equal to the one given");

	}

	/**
	 * Tests the method addPictoTags with null Tags and null Picto.
	 */
	@Test
	void addPictoTags_nullPictoTags_nothing() {
		assertEquals(null, pictoService.addPictoTags(null, null),
				"The PIcto returned should be equal to the one given");
	}

	/**
	 * Tests the method addPictoTags with empty Tags.
	 */
	@Test
	void addPictoTags_emptyTags_nothing() {
		Picto picto = new Picto();
		assertEquals(picto, pictoService.addPictoTags(picto, new HashSet<>()),
				"The PIcto returned should be equal to the one given");

	}

	/**
	 * Tests the method addPictoTags with data ok.
	 */
	@Test
	void addPictoTags_pictoTagsOk_add() {
		// Initiating picto
		Picto initialPicto = new Picto();
		Media media = new Media("parchemin.jpg", "Un parchemin");
		initialPicto.setMedia(media);
		Set<Tag> existingTags = Stream.of(new Tag("contrat"), new Tag("loi"), new Tag("signature"))
				.collect(Collectors.toSet());
		initialPicto.setTags(existingTags);

		// Initiating the tags to add
		Set<String> tagNamesToAdd = Stream.of("legislation", "paperasse").collect(Collectors.toSet());

		// Initiating data to verify
		Picto newPicto = pictoService.addPictoTags(initialPicto, tagNamesToAdd);
		Set<String> allTags = Stream.of("contrat", "loi", "signature", "legislation", "paperasse")
				.collect(Collectors.toSet());

		// Testing
		assertEquals(allTags.size(), newPicto.getTags().size(), "The tags should have the same size.");
		assertTrue(
				allTags.containsAll(newPicto.getTags().stream().map(tag -> tag.getTagId()).collect(Collectors.toSet())),
				"The tags should be the same.");
		assertEquals(media, newPicto.getMedia(), "The picto media should be the same.");

	}
}
