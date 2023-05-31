/**
 * 
 */
package com.searchpicto.ws.exception;

import com.searchpicto.ws.search.PictoIndexer;

/**
 * 
 * The exception thrown when an occurs while using {@link PictoIndexer}.
 * 
 * @author carol
 *
 */
public class PictoIndexingException extends Exception {
	
	/**
	 * Default Serial Version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * @param cause The detail of the exception thrown.
	 */
	public PictoIndexingException(Throwable cause) {
		super("Error while indexing pictos : ", cause);
	}
	
	/**
	 * Constructor.
	 * @param query The query.
	 * @param cause The detail of the exception thrown.
	 */
	public PictoIndexingException(String query, Throwable cause) {
		super("Error while looking for the picto with the query : " + query, cause);
	}
	
	/**
	 * Constructor.
	 * @param id The picto id.
	 * @param cause The detail of the exception thrown.
	 */
	public PictoIndexingException(Long pictoId, Throwable cause) {
		super("Error while indexing the picto with the id : " + pictoId, cause);
	}

}
