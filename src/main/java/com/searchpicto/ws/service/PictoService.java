package com.searchpicto.ws.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.searchpicto.ws.exception.PictoIndexingException;
import com.searchpicto.ws.model.Media;
import com.searchpicto.ws.model.Picto;
import com.searchpicto.ws.model.Tag;
import com.searchpicto.ws.search.PictoIndexer;

/**
 * The interface defines the services available for a {@link Media}.
 * 
 * @author carol
 *
 */
@Service
public interface PictoService {

	/**
	 * Retrieve all the {@link Picto} associated to a query search.
	 * 
	 * @param query The query search made by the user.
	 * @return The {@link List} of {@link Picto} associated.
	 * @throws PictoIndexingException If a problem occurs with {@link PictoIndexer}.
	 */
	List<Picto> findPictosByQuery(String query) throws PictoIndexingException;

	/**
	 * Retrieve a Picto by its id.
	 * 
	 * @param id The {@link Picto} id
	 * @return The {@link Picto}found.
	 */
	Optional<Picto> getPictoById(Long id);

	/**
	 * Add a new {@link Picto}.
	 * 
	 * @param picto The {@link Picto}to add.
	 * @throws PictoIndexingException The exception thrown if the indexing goes
	 *                                wrong.
	 */
	void addNewPicto(Picto picto) throws PictoIndexingException;

	/**
	 * Add {@link Tag} to a {@link Picto}.
	 * 
	 * @param picto   The {@link Picto}to be updated.
	 * @param newTags The tag values to be added.
	 * @throws PictoIndexingException The exception thrown if the indexing goes
	 *                                wrong.
	 * @return The {@link Picto}with th enew tags added.
	 */
	Picto addPictoTags(Picto picto, Set<String> newTags) throws PictoIndexingException;

	/**
	 * Retrieves the n last pictos added in the database.
	 * 
	 * @param sizeLimit The number of pictos wanted.
	 * @return The n pictos.
	 */
	List<Picto> getLastPictosAdded(int sizeLimit);

	/**
	 * Updates the index with all pictos.
	 * 
	 * @throws PictoIndexingException The exception thrown if the indexing goes
	 *                                wrong.
	 */
	void indexAllPictos() throws PictoIndexingException;

}
