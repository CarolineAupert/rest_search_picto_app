package com.searchpicto.ws.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.searchpicto.ws.model.Picto;
import com.searchpicto.ws.service.PictoService;

@RestController
public class PictoControllerImpl implements PictoController {

	@Autowired
	private PictoService pictoService;
	
	@Override
	public Set<Picto> findPictosByTag(String tag) {
		return pictoService.findPictosByTagName(tag);
	}

	@Override
	public Picto getPictoById(Long id) {
		//TODO Manage exception when no picto found.
		return pictoService.getPictoById(id);
	}

}
