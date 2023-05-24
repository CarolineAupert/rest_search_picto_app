package com.searchpicto.ws.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.searchpicto.ws.model.Picto;
import com.searchpicto.ws.model.Tag;
import com.searchpicto.ws.repository.PictoRepository;
import com.searchpicto.ws.repository.TagRepository;

/**
 * Implementation of {@link PictoService}.
 * 
 * @author carol
 *
 */
@Service
public class PictoServiceImpl implements PictoService {

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

	@Override
	public Optional<Picto> getPictoById(Long id) {
		Optional<Picto> picto = Optional.empty();
		if (id != null) {
			picto = pictoRepository.findById(id);
		}
		return picto;
	}

	@Override
	public Set<Picto> findPictosByTagName(String tagName) {
		Set<Picto> pictos = new HashSet<>();
		if (tagName != null && StringUtils.isNotEmpty(tagName) && StringUtils.isNotBlank(tagName)) {
			Optional<Tag> tag = tagRepository.findById(tagName);

			if (tag.isPresent()) {
				pictos = tag.get().getPictos();
			}
		}
		return pictos;
	}

	@Override
	public void addNewPicto(Picto picto) {
		if (picto != null && picto.getTags() != null) {
			picto.getTags().forEach(tag -> tagRepository.save(tag));
			pictoRepository.save(picto);
		}
	}

	@Override
	public Picto addPictoTags(Picto picto, Set<String> newTags) {
		if (picto != null && newTags != null && !newTags.isEmpty()) {
			Set<Tag> tagsToAdd = newTags.stream().map(Tag::new).collect(Collectors.toSet());
			picto.addTags(tagsToAdd);
			this.addNewPicto(picto);
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
