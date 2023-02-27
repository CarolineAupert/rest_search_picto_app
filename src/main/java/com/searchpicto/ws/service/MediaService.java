package com.searchpicto.ws.service;

import org.springframework.stereotype.Service;

import com.searchpicto.ws.model.Media;

/**
 * The interface defines the services available for a {@link Media}.
 * 
 * @author carol
 *
 */
@Service
public interface MediaService {

	/**
	 * Converts the image Media to a WebP.
	 * 
	 * @param media The {@link Media}to be converted.
	 * @return The converted {@link Media}.
	 */
	public Media convertToWebP(Media media);

	/**
	 * Check if the {@link Media}is compatible for being stored.
	 * 
	 * @param media The {@link Media} to be checked.
	 * @return True if the {@link Media}is ok, false otherwise.
	 */
	public boolean isImageCompatible(Media media);

}
