package com.searchpicto.ws.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.searchpicto.ws.model.Media;
import com.searchpicto.ws.model.Picto;
import com.searchpicto.ws.model.Tag;

@ExtendWith(MockitoExtension.class)
public class PictoIndexerTest {

	/**
	 * Delete the file index after each test.
	 * 
	 * @throws IOException The exception.
	 */
	@BeforeEach
	void deleteIndexBeforeTest() throws IOException {
		FileUtils.deleteDirectory(new File("./src/test/resources/luceneok/index"));
	}

	/**
	 * Delete the file index after each test.
	 * 
	 * @throws IOException The exception.
	 */
	@AfterEach
	void deleteIndexAfterTest() throws IOException {
		FileUtils.deleteDirectory(new File("./src/test/resources/luceneok/index"));
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
	}

	/**
	 * Analyzes a text.
	 * 
	 * @param text     The text to analyze.
	 * @param analyzer The analyzer.
	 * @return The words analyzed.
	 * @throws Exception The exception.
	 */
	private List<String> analyze(String text, Analyzer analyzer) throws Exception {
		var result = new ArrayList<String>();
		TokenStream tokenStream = analyzer.tokenStream("sampleName", text);
		CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);
		tokenStream.reset();
		while (tokenStream.incrementToken()) {
			result.add(attr.toString());
		}
		return result;
	}

	/**
	 * Method to init a Picto with Tags.
	 * 
	 * @param id       The Picto id..
	 * @param location The Media location.
	 * @param title    The Media title.
	 * @param tags     The Picto Tags.
	 * @return The created Picto.
	 */
	private Picto initPicto(Long id, String location, String title, Set<String> tags) {
		Picto picto = new Picto();
		picto.setPictoId(id);
		picto.setCreationDate(LocalDateTime.now());
		picto.setMedia(new Media(location, title));
		if (tags != null) {
			picto.setTags(tags.stream().map(Tag::new).collect(Collectors.toSet()));
		}
		return picto;
	}

	/**
	 * Retrieve in a Set the tags in contained in a Document.
	 * 
	 * @param doc The document.
	 * @return The Set of tags.
	 */
	private Set<String> getTagsIdFromDoc(Document doc) {
		String tagsId = doc.get(PictoIndexer.FIELD_QUERY);
		return new HashSet<String>(Arrays.asList(StringUtils.split(tagsId)));
	}

	/**
	 * Tests the method initCustomAnalyzer.
	 * 
	 * @throws Exception Exception when initalizing the analyzer
	 */
	@Test
	void initCustomAnalyzer_ok_FrenchLightStemFilterFactory() throws Exception {
		PictoIndexer pictoIndexer = new PictoIndexer("./src/test/resources/luceneOk");

		Analyzer pictoAnalyzer = pictoIndexer.getAnalyzer();

		List<String> result = analyze("Ceci est un test. J'analyse Lucène. C'est très apprenant.", pictoAnalyzer);

		assertTrue(result.containsAll(Arrays.asList("test", "analys", "lucen", "aprenant", "tre")),
				"It should contain the same words.");
	}

	/**
	 * Tests the method initCustomAnalyzer when there is no existing config folder.
	 * 
	 * @throws Exception Exception
	 */
	@Test
	void initCustomAnalyzer_ko_noConfigFolder() throws Exception {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
			new PictoIndexer("./src/test/resources/luceneEmpty");
		});

		assertTrue(thrown.getMessage().contains(".\\src\\test\\resources\\luceneEmpty\\config is not a directory"));
	}

	/**
	 * Tests the method initCustomAnalyzer when there is no existing stopwords
	 * file..
	 * 
	 * @throws Exception Exception
	 */
	@Test
	void initCustomAnalyzer_ko_noStopWordsFile() throws Exception {
		IOException thrown = assertThrows(IOException.class, () -> {
			new PictoIndexer("./src/test/resources/luceneNoStopWordsFile");
		});

		assertTrue(thrown.getMessage().contains("Resource not found"));
	}

	/**
	 * Tests the method createDocument with a Picto with several tags.
	 * 
	 * @throws Exception Exception
	 */
	@Test
	void createDocument_ok_severalTags() throws Exception {
		Picto picto = initPicto(Long.valueOf(2), "Loupe.jpg", "Une loupe",
				Stream.of("c'est", "loupe", "détail", "chercher", "analyser", "zoom").collect(Collectors.toSet()));

		PictoIndexer pictoIndexer = new PictoIndexer("./src/test/resources/luceneOk");
		// We don't want to test the analyzer for this test
		pictoIndexer.setAnalyzer(new StandardAnalyzer());
		Document doc = pictoIndexer.createDocument(picto);
		Set<String> expectedTags = picto.getTags().stream().map(Tag::getTagId).collect(Collectors.toSet());

		assertEquals(doc.get(PictoIndexer.FIELD_ID), picto.getPictoId().toString(),
				"The document does not correspond to the picto id.");
		assertTrue(expectedTags.containsAll(getTagsIdFromDoc(doc)), "The document does not contain the correct tags.");
	}

	/**
	 * Tests the method createDocument with a Picto with one tags.
	 * 
	 * @throws Exception Exception
	 */
	@Test
	void createDocument_ok_oneTag() throws Exception {
		Picto picto = initPicto(Long.valueOf(2), "Loupe.jpg", "Une loupe",
				Stream.of("c'est").collect(Collectors.toSet()));

		PictoIndexer pictoIndexer = new PictoIndexer("./src/test/resources/luceneOk");
		// We don't want to test the analyzer for this test
		pictoIndexer.setAnalyzer(new StandardAnalyzer());
		Document doc = pictoIndexer.createDocument(picto);
		Set<String> expectedTags = picto.getTags().stream().map(Tag::getTagId).collect(Collectors.toSet());

		assertEquals(doc.get(PictoIndexer.FIELD_ID), picto.getPictoId().toString(),
				"The document does not correspond to the picto id.");
		assertTrue(expectedTags.containsAll(getTagsIdFromDoc(doc)), "The document does not contain the correct tag.");
	}

	/**
	 * Tests the method createDocument with a Picto with one tags.
	 * 
	 * @throws Exception Exception
	 */
	@Test
	void createDocument_ko_emptyTags() throws Exception {
		Picto picto = initPicto(Long.valueOf(2), "Loupe.jpg", "Une loupe", new HashSet<String>());

		PictoIndexer pictoIndexer = new PictoIndexer("./src/test/resources/luceneOk");
		Document doc = pictoIndexer.createDocument(picto);

		assertTrue(doc.getFields().isEmpty(), "The document should be empty.");
	}

	/**
	 * Tests the method createDocument with a Picto with one tags.
	 * 
	 * @throws Exception Exception
	 */
	@Test
	void createDocument_ko_nullTags() throws Exception {
		Picto picto = initPicto(Long.valueOf(2), "Loupe.jpg", "Une loupe", null);

		PictoIndexer pictoIndexer = new PictoIndexer("./src/test/resources/luceneOk");
		Document doc = pictoIndexer.createDocument(picto);

		assertTrue(doc.getFields().isEmpty(), "The document should be empty.");
	}

	/**
	 * Tests the method createDocument with a null Picto.
	 * 
	 * @throws Exception Exception
	 */
	@Test
	void createDocument_ko_nullPicto() throws Exception {
		PictoIndexer pictoIndexer = new PictoIndexer("./src/test/resources/luceneOk");
		Document doc = pictoIndexer.createDocument(null);

		assertTrue(doc.getFields().isEmpty(), "The document should be empty.");
	}

	/**
	 * Tests the method search when there is no index.
	 * 
	 * @throws Exception Exception
	 */
	@Test
	void search_ko_noIndex() throws Exception {
		// init
		PictoIndexer pictoIndexer = new PictoIndexer("./src/test/resources/luceneOk");

		IndexNotFoundException thrown = assertThrows(IndexNotFoundException.class, () -> {
			pictoIndexer.search("test", PictoIndexer.FIELD_ID, 12);
		});

		assertTrue(thrown.getMessage().contains("no segments* file found"), "The error message is wrong.");
	}

	/**
	 * Tests the method search when the index is empty.
	 * 
	 * @throws Exception Exception
	 */
	@Test
	void search_ok_indexEmpty() throws Exception {
		// init
		PictoIndexer pictoIndexer = new PictoIndexer("./src/test/resources/luceneOk");
		pictoIndexer.indexObjects(new ArrayList<>());
		List<Document> docs = pictoIndexer.search("test", PictoIndexer.FIELD_ID, 12);

		assertTrue(docs.isEmpty(), "There should be no documents found.");
	}

	/**
	 * Tests when there is one picto stored.
	 * 
	 * @throws Exception Exception
	 */
	@Test
	void search_ok_onlyOnePicto() throws Exception {
		// init
		PictoIndexer pictoIndexer = new PictoIndexer("./src/test/resources/luceneOk");
		Picto picto = initPicto(Long.valueOf(2), "Loupe.jpg", "Une loupe",
				Stream.of("c'est", "loupe", "détail", "chercher", "analyser", "zoom").collect(Collectors.toSet()));
		pictoIndexer.indexObject(picto);
		List<Document> wrongSearchDocs = pictoIndexer.search("truc", PictoIndexer.FIELD_QUERY, 12);
		List<Document> goodSearchDocs = pictoIndexer.search("loupe", PictoIndexer.FIELD_QUERY, 12);

		assertTrue(wrongSearchDocs.isEmpty(), "There should be no documents found.");
		assertEquals(1, goodSearchDocs.size(), "There should be only one document found.");
		assertEquals("2", goodSearchDocs.get(0).get(PictoIndexer.FIELD_ID), "The picto found is wrong.");

	}

	/**
	 * Tests when there are several pictos stored.
	 * 
	 * @throws Exception Exception
	 */
	@Test
	void search_ok_severalPictos() throws Exception {
		// init
		PictoIndexer pictoIndexer = new PictoIndexer("./src/test/resources/luceneOk");
		Picto picto1 = initPicto(Long.valueOf(2), "Loupe.jpg", "Une loupe",
				Stream.of("c'est", "loupe", "détail", "chercher", "analyser", "contrats").collect(Collectors.toSet()));
		Picto picto2 = initPicto(Long.valueOf(5), "Parchemin.jpg", "Un parchemin",
				Stream.of("c'est", "loi", "détail", "contrat", "papier").collect(Collectors.toSet()));
		Picto picto3 = initPicto(Long.valueOf(9), "Perso.jpg", "Un perso",
				Stream.of("personnage", "homme", "femme", "humain").collect(Collectors.toSet()));
		pictoIndexer.indexObjects(Arrays.asList(picto1, picto2, picto3));

		List<Document> wrongSearchDocs = pictoIndexer.search("truc", PictoIndexer.FIELD_QUERY, 12);
		List<Document> goodSearchDocs = pictoIndexer.search("contrat", PictoIndexer.FIELD_QUERY, 12);

		assertTrue(wrongSearchDocs.isEmpty(), "There should be no documents found.");
		assertEquals(2, goodSearchDocs.size(), "The number of pictos found is wrong.");
	}

	/**
	 * Tests indexObject when no tags are present.
	 * 
	 * @throws Exception Exception
	 */
	@Test
	void indexObject_ko_tagsEmpty() throws Exception {
		// init
		PictoIndexer pictoIndexer = new PictoIndexer("./src/test/resources/luceneOk");
		Picto picto = initPicto(Long.valueOf(2), "Loupe.jpg", "Une loupe", new HashSet<>());
		pictoIndexer.indexObject(picto);

		List<Document> docs = pictoIndexer.search("contrat", PictoIndexer.FIELD_QUERY, 12);

		assertTrue(docs.isEmpty(), "There should be no documents found.");
	}

	/**
	 * Tests indexObject when no tags are present.
	 * 
	 * @throws Exception Exception
	 */
	@Test
	void indexObject_ko_nullId() throws Exception {
		// init
		PictoIndexer pictoIndexer = new PictoIndexer("./src/test/resources/luceneOk");
		Picto picto = initPicto(null, "Loupe.jpg", "Une loupe", new HashSet<>());
		pictoIndexer.indexObject(picto);

		List<Document> docs = pictoIndexer.search("contrat", PictoIndexer.FIELD_QUERY, 12);

		assertTrue(docs.isEmpty(), "There should be no documents found.");
	}

	/**
	 * Tests indexObject when picto is null.
	 * 
	 * @throws Exception Exception
	 */
	@Test
	void indexObject_ko_nullPicto() throws Exception {
		// init
		PictoIndexer pictoIndexer = new PictoIndexer("./src/test/resources/luceneOk");
		pictoIndexer.indexObject(null);

		List<Document> docs = pictoIndexer.search("contrat", PictoIndexer.FIELD_QUERY, 12);

		assertTrue(docs.isEmpty(), "There should be no documents found.");
	}

	/**
	 * Tests indexObjects when one picto is wrong among others.
	 * 
	 * @throws Exception Exception
	 */
	@Test
	void indexObjects_ok_withOnePictoko() throws Exception {
		// init
		PictoIndexer pictoIndexer = new PictoIndexer("./src/test/resources/luceneOk");
		Picto picto1 = initPicto(Long.valueOf(2), "Loupe.jpg", "Une loupe",
				Stream.of("c'est", "loupe", "détail", "chercher", "analyser", "contrats").collect(Collectors.toSet()));
		Picto picto2 = initPicto(Long.valueOf(5), "Parchemin.jpg", "Un parchemin", null);
		Picto picto3 = initPicto(Long.valueOf(9), "Perso.jpg", "Un perso",
				Stream.of("personnage", "homme", "femme", "humain").collect(Collectors.toSet()));
		pictoIndexer.indexObjects(Arrays.asList(picto1, picto2, picto3));

		List<Document> wrongSearchDocs = pictoIndexer.search("truc", PictoIndexer.FIELD_QUERY, 12);
		List<Document> goodSearchDocs = pictoIndexer.search("contrat", PictoIndexer.FIELD_QUERY, 12);

		assertTrue(wrongSearchDocs.isEmpty(), "There should be no documents found.");
		assertEquals(1, goodSearchDocs.size(), "The number of pictos found is wrong.");
		assertEquals("2", goodSearchDocs.get(0).get(PictoIndexer.FIELD_ID), "The picto found is wrong.");
	}

	/**
	 * Tests indexObjects with hits inferior to number of pictos.
	 * 
	 * @throws Exception Exception
	 */
	@Test
	void indexObjects_ok_hits() throws Exception {
		// init
		PictoIndexer pictoIndexer = new PictoIndexer("./src/test/resources/luceneOk");
		Picto picto1 = initPicto(Long.valueOf(2), "Loupe.jpg", "Une loupe",
				Stream.of("c'est", "loupe", "détail", "chercher", "analyser", "contrats").collect(Collectors.toSet()));
		Picto picto2 = initPicto(Long.valueOf(5), "Parchemin.jpg", "Un parchemin",
				Stream.of("c'est", "loi", "détail", "contrat", "papier").collect(Collectors.toSet()));
		Picto picto3 = initPicto(Long.valueOf(8), "Perso.jpg", "Un perso",
				Stream.of("personnage", "homme", "femme", "humain").collect(Collectors.toSet()));
		Picto picto4 = initPicto(Long.valueOf(9), "Picto.jpg", "Un perso",
				Stream.of("Contrat", "homme", "femme", "humain").collect(Collectors.toSet()));
		pictoIndexer.indexObjects(Arrays.asList(picto1, picto2, picto3, picto4));

		List<Document> goodSearchDocs = pictoIndexer.search("contrat", PictoIndexer.FIELD_QUERY, 2);

		assertEquals(2, goodSearchDocs.size(), "The number of pictos found is wrong.");
	}

	/**
	 * Tests updateObject.
	 * 
	 * @throws Exception Exception
	 */
	@Test
	void updateObject_ok() throws Exception {
		// init
		PictoIndexer pictoIndexer = new PictoIndexer("./src/test/resources/luceneOk");
		Picto picto1 = initPicto(Long.valueOf(2), "Loupe.jpg", "Une loupe",
				Stream.of("c'est", "loupe", "détail", "chercher", "analyser", "contrats").collect(Collectors.toSet()));
		Picto picto2 = initPicto(Long.valueOf(5), "Parchemin.jpg", "Un parchemin",
				Stream.of("c'est", "loi", "détail", "contrat", "papier").collect(Collectors.toSet()));
		Picto picto3 = initPicto(Long.valueOf(8), "Perso.jpg", "Un perso",
				Stream.of("personnage", "homme", "femme", "humain").collect(Collectors.toSet()));
		Picto picto4 = initPicto(Long.valueOf(9), "Picto.jpg", "Un perso",
				Stream.of("Contrat", "homme", "femme", "humain").collect(Collectors.toSet()));
		pictoIndexer.indexObjects(Arrays.asList(picto1, picto2, picto3, picto4));

		List<Document> docsBeforeUpdate = pictoIndexer.search("contrat", PictoIndexer.FIELD_QUERY, 10);
		assertEquals(3, docsBeforeUpdate.size(), "The number of pictos found is wrong.");

		Picto pictoUpdated = initPicto(Long.valueOf(9), "Picto.jpg", "Un perso",
				Stream.of("machin", "truc", "bidule", "encore").collect(Collectors.toSet()));
		pictoIndexer.updateObjectIndex(pictoUpdated);

		List<Document> docsAfterUpdate = pictoIndexer.search("contrat", PictoIndexer.FIELD_QUERY, 10);
		assertEquals(2, docsAfterUpdate.size(), "The number of pictos found is wrong.");

	}

	/**
	 * Tests updateObject with a picto without tags.
	 * 
	 * @throws Exception Exception
	 */
	@Test
	void updateObject_ko_noTags() throws Exception {
		// init
		PictoIndexer pictoIndexer = new PictoIndexer("./src/test/resources/luceneOk");
		Picto picto1 = initPicto(Long.valueOf(2), "Loupe.jpg", "Une loupe",
				Stream.of("c'est", "loupe", "détail", "chercher", "analyser", "contrats").collect(Collectors.toSet()));
		Picto picto2 = initPicto(Long.valueOf(5), "Parchemin.jpg", "Un parchemin",
				Stream.of("c'est", "loi", "détail", "contrat", "papier").collect(Collectors.toSet()));
		Picto picto3 = initPicto(Long.valueOf(8), "Perso.jpg", "Un perso",
				Stream.of("personnage", "homme", "femme", "humain").collect(Collectors.toSet()));
		Picto picto4 = initPicto(Long.valueOf(9), "Picto.jpg", "Un perso",
				Stream.of("Contrat", "homme", "femme", "humain").collect(Collectors.toSet()));
		pictoIndexer.indexObjects(Arrays.asList(picto1, picto2, picto3, picto4));

		List<Document> docsBeforeUpdate = pictoIndexer.search("contrat", PictoIndexer.FIELD_QUERY, 10);
		assertEquals(3, docsBeforeUpdate.size(), "The number of pictos found is wrong.");

		Picto pictoUpdated = initPicto(Long.valueOf(9), "Picto.jpg", "Un perso", null);
		pictoIndexer.updateObjectIndex(pictoUpdated);

		List<Document> docsAfterUpdate = pictoIndexer.search("contrat", PictoIndexer.FIELD_QUERY, 10);
		assertEquals(2, docsAfterUpdate.size(), "The number of pictos found is wrong.");

	}

	/**
	 * Tests updateObject with a null picto for update.
	 * 
	 * @throws Exception Exception
	 */
	@Test
	void updateObject_ko_nullPicto() throws Exception {
		// init
		PictoIndexer pictoIndexer = new PictoIndexer("./src/test/resources/luceneOk");
		Picto picto1 = initPicto(Long.valueOf(2), "Loupe.jpg", "Une loupe",
				Stream.of("c'est", "loupe", "détail", "chercher", "analyser", "contrats").collect(Collectors.toSet()));
		Picto picto2 = initPicto(Long.valueOf(5), "Parchemin.jpg", "Un parchemin",
				Stream.of("c'est", "loi", "détail", "contrat", "papier").collect(Collectors.toSet()));
		Picto picto3 = initPicto(Long.valueOf(8), "Perso.jpg", "Un perso",
				Stream.of("personnage", "homme", "femme", "humain").collect(Collectors.toSet()));
		Picto picto4 = initPicto(Long.valueOf(9), "Picto.jpg", "Un perso",
				Stream.of("Contrat", "homme", "femme", "humain").collect(Collectors.toSet()));
		pictoIndexer.indexObjects(Arrays.asList(picto1, picto2, picto3, picto4));

		List<Document> docsBeforeUpdate = pictoIndexer.search("contrat", PictoIndexer.FIELD_QUERY, 10);
		assertEquals(3, docsBeforeUpdate.size(), "The number of pictos found is wrong.");

		pictoIndexer.updateObjectIndex(null);

		List<Document> docsAfterUpdate = pictoIndexer.search("contrat", PictoIndexer.FIELD_QUERY, 10);
		assertEquals(3, docsAfterUpdate.size(), "The number of pictos found is wrong.");

	}

}
