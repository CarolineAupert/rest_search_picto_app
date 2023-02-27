/**
 * 
 */
package com.searchpicto.ws.exception;

import com.searchpicto.ws.model.Picto;

/**
 * 
 * The exception thrown when no {@link Picto} is found for an id.
 * 
 * @author carol
 *
 */
public class PictoNotFoundException extends RuntimeException {
	
	/**
	 * Default Serial Version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * @param id The picto id.
	 */
	public PictoNotFoundException(Long id) {
		super("Could not find picto with id : " + id);
	}
	
	/**
	 * Constructor.
	 * @param id The picto tag.
	 */
	public PictoNotFoundException(String tag) {
		super("Could not find picto for the tag : " + tag);
	}

}
