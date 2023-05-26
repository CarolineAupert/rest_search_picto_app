package com.searchpicto.ws.search;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.fr.FrenchLightStemFilterFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.apache.lucene.analysis.util.ElisionFilterFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.Term;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.searchpicto.ws.model.Picto;
import com.searchpicto.ws.model.Tag;

/**
 * Implementation of {@link LuceneIndexer}for a {@link Picto}.
 * 
 * @author carol
 *
 */
@Service
public class PictoIndexer extends LuceneIndexer<Picto> {

	/**
	 * The field over which querying.
	 */
	public static final String FIELD_QUERY = "tagId";

	/**
	 * The field id.
	 */
	public static final String FIELD_ID = "pictoId";

	/**
	 * Number of hits
	 */
	public static final int HITS = 50;

	/**
	 * Constructor
	 * 
	 * @param luceneDirectory Lucene directory for index.
	 * @throws IOException The exception thrown if there is a problem with the
	 *                     directory.
	 */
	public PictoIndexer(@Value("${lucene.directory}") String luceneDirectory) throws IOException {
		super(luceneDirectory);
	}

	@Override
	public void indexObjects(List<Picto> pictos) throws IOException {
		var docsToIndex = new ArrayList<Document>();
		if (pictos != null) {
			pictos.forEach(picto -> docsToIndex.add(createDocument(picto)));
		}
		indexDocuments(docsToIndex);
	}

	@Override
	public void indexObject(Picto picto) throws IOException {
		var docsToIndex = new ArrayList<Document>();
		if (picto != null) {
			docsToIndex.add(createDocument(picto));
		}
		indexDocuments(docsToIndex);
	}

	@Override
	public void updateObjectIndex(Picto picto) throws IOException {
		if (picto != null && picto.getPictoId() != null) {
			updateDocument(new Term(FIELD_ID, picto.getPictoId().toString()), createDocument(picto));
		}

	}

	@Override
	public Document createDocument(Picto picto) {
		if (picto != null && picto.getTags() != null && !picto.getTags().isEmpty()) {
			Long pictoId = picto.getPictoId();
			Document doc = createPictoDocument(pictoId, picto.getTags());
			return doc;
		}
		return new Document();
	}

	/**
	 * Create a document from a {@link Picto}.
	 * 
	 * @param pictoId The picto id.
	 * @param tags    The tags.
	 * @return A Document.
	 */
	private Document createPictoDocument(Long pictoId, Set<Tag> tags) {
		var doc = new Document();
		String tagsToString = tags.stream().map(Tag::getTagId).collect(Collectors.joining(" "));
		doc.add(new TextField(FIELD_QUERY, tagsToString, Field.Store.YES));
		doc.add(new TextField(FIELD_ID, pictoId.toString(), Field.Store.YES));
		return doc;
	}

	@Override
	protected Analyzer initCustomAnalyzer() throws IOException {
		return CustomAnalyzer.builder(Paths.get(getConfigDirectory())).withTokenizer(StandardTokenizerFactory.NAME)
				.addTokenFilter(ElisionFilterFactory.NAME).addTokenFilter(FrenchLightStemFilterFactory.NAME)
				.addTokenFilter(LowerCaseFilterFactory.NAME).addTokenFilter(ASCIIFoldingFilterFactory.NAME)
				.addTokenFilter(StopFilterFactory.NAME, "ignoreCase", "false", "words", "stopwords.txt", "format",
						"snowball")
				.build();
	}

}
