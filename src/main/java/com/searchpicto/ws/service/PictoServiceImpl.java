package com.searchpicto.ws.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.searchpicto.ws.model.Picto;
import com.searchpicto.ws.model.Tag;
import com.searchpicto.ws.repository.PictoRepository;
import com.searchpicto.ws.repository.TagRepository;

@Service
public class PictoServiceImpl implements PictoService {

	@Autowired
	private TagRepository tagRepository;

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
			for (Tag tag : picto.getTags()) {
				tagRepository.save(tag);
			}
			pictoRepository.save(picto);
		}
	}

	@Override
	public Picto addPictoTags(Picto picto, Set<String> newTags) {
		if(picto != null && newTags!= null && !newTags.isEmpty()) {
			Set<Tag> tagsToAdd = newTags.stream().map(tag -> new Tag(tag)).collect(Collectors.toSet());
			picto.addTags(tagsToAdd);
			this.addNewPicto(picto);
		}
		return picto;
	}

}
