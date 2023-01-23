package com.searchpicto.ws.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.searchpicto.ws.model.Picto;

@Service
public interface PictoService {
	
	public Set<Picto> findPictosByTagName(String tagName);
	
	public Picto getPictoById(Long id);

}
