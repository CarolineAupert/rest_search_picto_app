package com.searchpicto.ws.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.Term;
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
	 */
	public PictoIndexer(){
		super();
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
	protected Analyzer initCustomAnalyzer() {
		return new CustomFrenchAnalyzer();
	}

}
