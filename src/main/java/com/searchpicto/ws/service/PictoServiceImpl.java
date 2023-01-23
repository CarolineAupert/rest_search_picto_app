package com.searchpicto.ws.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.searchpicto.ws.model.Picto;
import com.searchpicto.ws.model.Tag;
import com.searchpicto.ws.repository.PictoRepository;
import com.searchpicto.ws.repository.TagRepository;

@Service
public class PictoServiceImpl implements PictoService{
	
	@Autowired
	private TagRepository tagRepository;
	
	@Autowired
	private PictoRepository pictoRepository;
	
	@Override
	public Picto getPictoById(Long id) {
		return pictoRepository.findById(id).get();
	}
	
	@Override
	public Set<Picto> findPictosByTagName(String tagName) {
		Set<Picto> pictos = new HashSet<>();
		Optional<Tag> tag = tagRepository.findById(tagName);
		
		if (tag.isPresent()) {
			pictos = tag.get().getPictos();
		}
		return pictos;
	}


}
