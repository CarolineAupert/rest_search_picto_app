/**
 * 
 */
package com.searchpicto.ws.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
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

import com.searchpicto.ws.exception.PictoIndexingException;
import com.searchpicto.ws.model.Media;
import com.searchpicto.ws.model.Picto;
import com.searchpicto.ws.model.Tag;
import com.searchpicto.ws.repository.PictoRepository;
import com.searchpicto.ws.repository.TagRepository;
import com.searchpicto.ws.search.PictoIndexer;

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
	 * The mock TagRepository.
	 */
	@Mock
	private TagRepository tagRepositoryMock;

	/**
	 * The mock PictoIndexer.
	 */
	@Mock
	private PictoIndexer pictoIndexerMock;

	/**
	 * The mock PictoRepository.
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
	 * Init a document with the picto id.
	 * 
	 * @param pictoID The picto id.
	 * @return The document.
	 */
	private Document initDocument(String pictoID) {
		var doc = new Document();
		doc.add(new TextField(PictoIndexer.FIELD_ID, pictoID, Field.Store.YES));
		return doc;
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
	void addNewPicto_pictoNoTags_save() throws Exception {
		Picto picto = new Picto();
		picto.setTags(new HashSet<>());
		Picto pictoSaved = new Picto();

		Mockito.when(pictoRepositoryMock.save(picto)).thenReturn(pictoSaved);
		pictoService.addNewPicto(picto);

		verify(tagRepositoryMock, never()).save(any());
		verify(pictoIndexerMock).indexObject(pictoSaved);
	}

	/**
	 * Tests the method addNewPicto with a Picto with tags.
	 */
	@Test
	void addNewPicto_pictoWithTags_save() throws Exception {
		Picto picto = initPicto("Loupe.jpg", "Une loupe",
				Stream.of("loupe", "détail", "chercher", "analyser", "zoom").collect(Collectors.toSet()));
		Picto pictoSaved = new Picto();

		Mockito.when(pictoRepositoryMock.save(picto)).thenReturn(pictoSaved);
		pictoService.addNewPicto(picto);

		verify(tagRepositoryMock, times(5)).save(any());
		verify(pictoIndexerMock).indexObject(pictoSaved);

	}

	/**
	 * Tests the method addNewPicto with a Picto with tags.
	 */
	@Test
	void addNewPicto_pictoWithTags_indexerException() throws Exception {
		Picto picto = initPicto("Loupe.jpg", "Une loupe",
				Stream.of("loupe", "détail", "chercher", "analyser", "zoom").collect(Collectors.toSet()));
		
		Picto pictoSaved = new Picto();
		Mockito.when(pictoRepositoryMock.save(picto)).thenReturn(pictoSaved);
		Mockito.doThrow(new IOException("Picto Indexer Exception")).when(pictoIndexerMock).indexObject(pictoSaved);
		PictoIndexingException thrown = assertThrows(PictoIndexingException.class, () -> {
			pictoService.addNewPicto(picto);
		});

		verify(tagRepositoryMock, times(5)).save(any());
		verify(pictoIndexerMock).indexObject(pictoSaved);

		assertTrue(thrown.getMessage().contains("Error while indexing the picto with the id : "));
	}

	/**
	 * Tests the method addPictoTags with a null Picto.
	 */
	@Test
	void addPictoTags_nullPicto_nothing() throws Exception {
		assertEquals(null, pictoService.addPictoTags(null, new HashSet<>()),
				"The Picto returned should be equal to the one given");
	}

	/**
	 * Tests the method addPictoTags with null Tags.
	 */
	@Test
	void addPictoTags_nullTags_nothing() throws Exception {
		Picto picto = new Picto();
		assertEquals(picto, pictoService.addPictoTags(picto, null),
				"The Picto returned should be equal to the one given");

	}

	/**
	 * Tests the method addPictoTags with null Tags and null Picto.
	 */
	@Test
	void addPictoTags_nullPictoTags_nothing() throws Exception {
		assertEquals(null, pictoService.addPictoTags(null, null),
				"The Picto returned should be equal to the one given");
	}

	/**
	 * Tests the method addPictoTags with empty Tags.
	 */
	@Test
	void addPictoTags_emptyTags_nothing() throws Exception {
		Picto picto = new Picto();
		assertEquals(picto, pictoService.addPictoTags(picto, new HashSet<>()),
				"The Picto returned should be equal to the one given");

	}

	/**
	 * Tests the method addPictoTags with data ok.
	 */
	@Test
	void addPictoTags_pictoTagsOk_add() throws Exception {
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
		verify(pictoIndexerMock).updateObjectIndex(newPicto);

	}

	/**
	 * Tests the method addPictoTags with data ok.
	 */
	@Test
	void addPictoTags_pictoTagsOk_add_IndexerException() throws Exception {
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
		Mockito.doThrow(new IOException("Picto Indexer Exception")).when(pictoIndexerMock).updateObjectIndex(any());

		PictoIndexingException thrown = assertThrows(PictoIndexingException.class, () -> {
			pictoService.addPictoTags(initialPicto, tagNamesToAdd);
		});

		assertTrue(thrown.getMessage().contains("Error while indexing the picto with the id : "));

	}

	/**
	 * Tests the method findPictosByQuery when the query is empty.
	 */
	@Test
	void findPictosByQuery_emptyQuery() throws Exception {
		List<Picto> pictos = pictoService.findPictosByQuery("");

		verify(pictoRepositoryMock, never()).findById(anyLong());
		verify(pictoIndexerMock, never()).search(anyString(), anyString(), anyInt());

		assertTrue(pictos.isEmpty(), "No pictos should be found.");
	}

	/**
	 * Tests the method findPictosByQuery when the query is null.
	 */
	@Test
	void findPictosByQuery_nullQuery() throws Exception {
		List<Picto> pictos = pictoService.findPictosByQuery(null);

		verify(pictoRepositoryMock, never()).findById(anyLong());
		verify(pictoIndexerMock, never()).search(anyString(), anyString(), anyInt());

		assertTrue(pictos.isEmpty(), "No pictos should be found.");
	}

	/**
	 * Tests the method findPictosByQuery when the indexer throws an exception.
	 */
	@Test
	void findPictosByQuery_indexerException() throws Exception {
		String query = "truc";
		Mockito.doThrow(new IOException("Picto Indexer Exception")).when(pictoIndexerMock).search(query,
				PictoIndexer.FIELD_QUERY, PictoIndexer.HITS);

		PictoIndexingException thrown = assertThrows(PictoIndexingException.class, () -> {
			pictoService.findPictosByQuery(query);
		});

		verify(pictoRepositoryMock, never()).findById(anyLong());

		assertTrue(thrown.getMessage().contains("Error while looking for the picto with the query : truc"));
	}

	/**
	 * Tests the method findPictosByQuery when null is returned by the indexer.
	 */
	@Test
	void findPictosByQuery_nullPictoFound_indexer() throws Exception {
		String query = "perso";
		Mockito.when(pictoIndexerMock.search(query, PictoIndexer.FIELD_QUERY, PictoIndexer.HITS)).thenReturn(null);
		List<Picto> pictos = pictoService.findPictosByQuery(query);

		verify(pictoRepositoryMock, never()).findById(anyLong());

		assertTrue(pictos.isEmpty(), "No pictos should be found.");
	}

	/**
	 * Tests the method findPictosByQuery when no pictos are found by the indexer.
	 */
	@Test
	void findPictosByQuery_noPictoFound_indexer() throws Exception {
		String query = "perso";
		Mockito.when(pictoIndexerMock.search(query, PictoIndexer.FIELD_QUERY, PictoIndexer.HITS))
				.thenReturn(new ArrayList<Document>());
		List<Picto> pictos = pictoService.findPictosByQuery(query);

		verify(pictoRepositoryMock, never()).findById(anyLong());

		assertTrue(pictos.isEmpty(), "No pictos should be found.");
	}

	/**
	 * Tests the method findPictosByQuery when no pictos are found by the indexer.
	 */
	@Test
	void findPictosByQuery_noPictoFound_repo() throws Exception {
		// Init
		String query = "perso";
		Long pictoId = Long.valueOf(45);
		Document doc = initDocument(pictoId.toString());
		var docs = new ArrayList<Document>();
		docs.add(doc);

		Mockito.when(pictoIndexerMock.search(query, PictoIndexer.FIELD_QUERY, PictoIndexer.HITS)).thenReturn(docs);
		Mockito.when(pictoRepositoryMock.findById(pictoId)).thenReturn(Optional.empty());

		List<Picto> pictos = pictoService.findPictosByQuery(query);

		assertTrue(pictos.isEmpty(), "No pictos should be found.");
	}

	/**
	 * Tests the method findPictosByQuery when one picto is found.
	 */
	@Test
	void findPictosByQuery_onePictoFound() throws Exception {
		// Init
		String query = "perso";
		Long pictoId = Long.valueOf(45);
		Document doc = initDocument(pictoId.toString());
		var docs = new ArrayList<Document>();
		docs.add(doc);
		Picto picto = initPicto("Loupe.jpg", "Une loupe",
				Stream.of("loupe", "détail", "chercher", "analyser", "zoom").collect(Collectors.toSet()));

		Mockito.when(pictoIndexerMock.search(query, PictoIndexer.FIELD_QUERY, PictoIndexer.HITS)).thenReturn(docs);
		Mockito.when(pictoRepositoryMock.findById(pictoId)).thenReturn(Optional.of(picto));

		List<Picto> pictos = pictoService.findPictosByQuery(query);

		assertEquals(pictos.size(), 1, "One picto should be found.");
		assertTrue(pictos.contains(picto), "One picto should be found.");
	}

	/**
	 * Tests the method findPictosByQuery when several pictos are found.
	 */
	@Test
	void findPictosByQuery_oseveralPictoFound() throws Exception {
		// Init
		String query = "perso";
		Long pictoId1 = Long.valueOf(45);
		Long pictoId2 = Long.valueOf(25);
		Document doc1 = initDocument(pictoId1.toString());
		Document doc2 = initDocument(pictoId2.toString());
		var docs = new ArrayList<Document>();
		docs.add(doc1);
		docs.add(doc2);
		Picto picto1 = initPicto("Loupe.jpg", "Une loupe",
				Stream.of("loupe", "détail", "chercher", "analyser", "zoom").collect(Collectors.toSet()));
		Picto picto2 = initPicto("Parchemin.jpg", "Un parchemin",
				Stream.of("parchemin", "détail", "contrat", "législation").collect(Collectors.toSet()));

		Mockito.when(pictoIndexerMock.search(query, PictoIndexer.FIELD_QUERY, PictoIndexer.HITS)).thenReturn(docs);
		Mockito.when(pictoRepositoryMock.findById(pictoId1)).thenReturn(Optional.of(picto1));
		Mockito.when(pictoRepositoryMock.findById(pictoId2)).thenReturn(Optional.of(picto2));

		List<Picto> pictos = pictoService.findPictosByQuery(query);

		assertEquals(pictos.size(), 2, "One picto should be found.");
		assertTrue(pictos.contains(picto1), "The correspondong picto should be found.");
		assertTrue(pictos.contains(picto2), "The correspondong picto should be found.");
	}

}
