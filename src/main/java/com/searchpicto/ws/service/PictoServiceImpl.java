package com.searchpicto.ws.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.searchpicto.ws.exception.PictoIndexingException;
import com.searchpicto.ws.model.Picto;
import com.searchpicto.ws.model.Tag;
import com.searchpicto.ws.repository.PictoRepository;
import com.searchpicto.ws.repository.TagRepository;
import com.searchpicto.ws.search.PictoIndexer;

/**
 * Implementation of {@link PictoService}.
 * 
 * @author carol
 *
 */
@Service
public class PictoServiceImpl implements PictoService {

	/**
	 * The logger.
	 */
	Logger logger = LoggerFactory.getLogger(PictoServiceImpl.class);

	/**
	 * Tag repository.
	 */
	@Autowired
	private TagRepository tagRepository;

	/**
	 * Picto Repository.
	 */
	@Autowired
	private PictoRepository pictoRepository;

	/**
	 * The Picto indexer.
	 */
	@Autowired
	private PictoIndexer pictoIndexer;

	@Override
	public Optional<Picto> getPictoById(Long id) {
		Optional<Picto> picto = Optional.empty();
		if (id != null) {
			picto = pictoRepository.findById(id);
		}
		return picto;
	}

	@Override
	public List<Picto> findPictosByQuery(String query) throws PictoIndexingException {
		var pictos = new ArrayList<Picto>();
		if (query != null && StringUtils.isNotEmpty(query) && StringUtils.isNotBlank(query)) {
			try {
				// See to make it pageable when the number of picto increases much ?
				List<Document> docs = pictoIndexer.search(query, PictoIndexer.FIELD_QUERY, PictoIndexer.HITS);
				if (docs != null && !docs.isEmpty()) {
					for (Document doc : docs) {
						Long pictoID = Long.valueOf(doc.get(PictoIndexer.FIELD_ID));
						Optional<Picto> picto = pictoRepository.findById(pictoID);
						if (picto.isPresent()) {
							pictos.add(picto.get());
						}
					}
				}
			} catch (IOException | ParseException e) {
				String.format("Error while accessing the index for the query \"%s\" : %s", query, e);
				throw new PictoIndexingException(query, e);
			}

		}
		return pictos;
	}

	@Override
	public void addNewPicto(Picto picto) throws PictoIndexingException {
		if (picto != null && picto.getTags() != null) {
			picto.getTags().forEach(tag -> tagRepository.save(tag));
			Picto pictoCreated = pictoRepository.save(picto);
			try {
				pictoIndexer.indexObject(pictoCreated);
			} catch (IOException e) {
				String.format("Error while indexing the picto with id \"%d\" : %s", pictoCreated.getPictoId(), e);
				throw new PictoIndexingException(pictoCreated.getPictoId(), e);
			}
		}
	}

	@Override
	public Picto addPictoTags(Picto picto, Set<String> newTags) throws PictoIndexingException {
		// See if this is in one session.
		if (picto != null && newTags != null && !newTags.isEmpty()) {
			// Save tags
			Set<Tag> tagsToAdd = newTags.stream().map(Tag::new).collect(Collectors.toSet());
			tagsToAdd.forEach(tag -> tagRepository.save(tag));

			// Save picto
			picto.addTags(tagsToAdd);
			pictoRepository.save(picto);

			try {
				// TODO : si exception alors le logger pour pouvoir s'en occuper plus tard !
				// Update index
				pictoIndexer.updateObjectIndex(picto);
			} catch (IOException e) {
				String.format("Error while updating the index of the picto with id \"%d\" : %s", picto.getPictoId(), e);
				throw new PictoIndexingException(picto.getPictoId(), e);
			}
		}
		return picto;
	}

	@Override
	public List<Picto> getLastPictosAdded(int sizeLimit) {
		if (sizeLimit > 0) {
			Page<Picto> pictosPage = pictoRepository
					.findAll(PageRequest.of(0, sizeLimit, Sort.by(Sort.Direction.DESC, "creationDate")));
			if (pictosPage != null) {
				return pictosPage.getContent();
			}
		}
		return new ArrayList<>();
	}

}
