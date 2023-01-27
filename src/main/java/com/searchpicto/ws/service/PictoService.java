package com.searchpicto.ws.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.searchpicto.ws.model.Media;
import com.searchpicto.ws.model.Picto;
import com.searchpicto.ws.model.Tag;

/**
 * The interface defines the services available for a {@link Media}.
 * 
 * @author carol
 *
 */
@Service
public interface PictoService {

	/**
	 * Retrieve all the {@link Picto} associated to a {@link Tag}.
	 * 
	 * @param tagName The {@link Tag} id/value
	 * @return The {@link Set} of {@link Picto} associated.
	 */
	public Set<Picto> findPictosByTagName(String tagName);

	/**
	 * Retrieve a Picto by its id.
	 * 
	 * @param id The {@link Picto} id
	 * @return The {@link Picto}found.
	 */
	public Optional<Picto> getPictoById(Long id);

	/**
	 * Add a new {@link Picto}.
	 * 
	 * @param picto The {@link Picto}to add.
	 */
	public void addNewPicto(Picto picto);

	/**
	 * Add {@link Tag} to a {@link Picto}.
	 * 
	 * @param picto   The {@link Picto}to be updated.
	 * @param newTags The tag values to be added.
	 * @return The {@link Picto}with th enew tags added.
	 */
	public Picto addPictoTags(Picto picto, Set<String> newTags);

}
