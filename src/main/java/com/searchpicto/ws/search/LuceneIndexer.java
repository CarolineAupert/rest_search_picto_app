package com.searchpicto.ws.search;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

/**
 * 
 * Abstract class made for indexing objects based on Lucene.
 * 
 * @author carol
 *
 * @param <T>
 */
@Service
public abstract class LuceneIndexer<T> {

	/**
	 * The custom analyzer.
	 */
	private Analyzer customAnalyzer;
	/**
	 * The lucene directory where to store indexes and config.
	 */
	private String luceneDirectory;

	/**
	 * Constructor.
	 * 
	 * @param luceneDirectory The lucene directory to initalize.
	 * @throws IOException Execption thown if the analyzer cannot find the config
	 *                     files.
	 */
	public LuceneIndexer(String luceneDirectory) throws IOException {
		this.luceneDirectory = luceneDirectory;
		this.customAnalyzer = initCustomAnalyzer();
	}

	/**
	 * Methods to index several documents.
	 * 
	 * @param documents The documents to index.
	 * @throws IOException The exception thrown if the is a problem accessing index.
	 */
	protected void indexDocuments(List<Document> documents) throws IOException {
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(getAnalyzer());
		IndexWriter writer = new IndexWriter(getIndexDirectory(), indexWriterConfig);
		for (var doc : documents) {
			writer.addDocument(doc);
		}
		writer.close();
	}
	

	/**
	 * Update a document index.
	 * @param term The term of the document to update.
	 * @param document The document to update.
	 * @throws IOException he exception thrown if the is a problem accessing index.
	 */
	protected void updateDocument(Term term, Document document) throws IOException {
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(getAnalyzer());
		IndexWriter writer = new IndexWriter(getIndexDirectory(), indexWriterConfig);
		writer.updateDocument(term, document);
		writer.close();
		
	}

	/**
	 * Method to make a search from a query string.
	 * 
	 * @param queryString The query string.
	 * @param fieldId     The field id to search.
	 * @param hitsPerPage Number of hits to show.
	 * @return The documents found.
	 * @throws ParseException The exception thrown if the is a problem when parsing
	 *                        the query.
	 * @throws IOException    The exception thrown if the is a problem accessing
	 *                        index.
	 */
	public List<Document> search(String queryString, String fieldId, int hitsPerPage)
			throws ParseException, IOException {
		Query query = new QueryParser(fieldId, getAnalyzer()).parse(queryString);

		IndexReader indexReader = DirectoryReader.open(getIndexDirectory());
		IndexSearcher searcher = new IndexSearcher(indexReader);
		TopDocs hits = searcher.search(query, hitsPerPage);
		StoredFields storeFields = searcher.storedFields();

		List<Document> documents = new ArrayList<>();
		for (ScoreDoc hit : hits.scoreDocs) {
			documents.add(storeFields.document(hit.doc));
		}

		return documents;
	}

	/**
	 * Create a document from a specific object.
	 * 
	 * @param object The object to convert.
	 * @return The document created.
	 */
	public abstract Document createDocument(T object);

	/**
	 * Index an object.
	 * 
	 * @param object The object to index.
	 * @throws IOException The exception thrown if the is a problem accessing index.
	 */
	public abstract void indexObject(T object) throws IOException;
	
	/**
	 * Update the index of an object.
	 * 
	 * @param object The object to update in the index.
	 * @throws IOException The exception thrown if the is a problem accessing index.
	 */
	public abstract void updateObjectIndex(T object) throws IOException;

	/**
	 * Index objects.
	 * 
	 * @param objects The objects to index.
	 * @throws IOException The exception thrown if the is a problem accessing index.
	 */
	public abstract void indexObjects(List<T> objects) throws IOException;

	/**
	 * analyzer getter.
	 *
	 * @return the analyzer.
	 */
	protected Analyzer getAnalyzer() {
		return customAnalyzer;
	}

	/**
	 * analyzer setter.
	 * 
	 * @param customAnalyser The analyzer to set.
	 */
	protected void setAnalyzer(Analyzer customAnalyser) {
		this.customAnalyzer = customAnalyser;
	}

	/**
	 * directory getter.
	 *
	 * @return the directory.
	 * @throws IOException The exception thrown if the is a problem accessing path.
	 */
	protected Directory getIndexDirectory() throws IOException {
		String indexDirectory = luceneDirectory + "/index";
		return FSDirectory.open(Paths.get(indexDirectory));
	}

	/**
	 * Config directory getter.
	 * 
	 * @return The directory.
	 * @throws IOException The exception thrown if the is a problem accessing path.
	 */
	protected String getConfigDirectory() throws IOException {
		return luceneDirectory + "/config";
	}

	/**
	 * Initializes the analyzer.
	 *
	 * @throws IOException The exception thrown if the is a problem accessing path.
	 */
	protected abstract Analyzer initCustomAnalyzer() throws IOException;

}
