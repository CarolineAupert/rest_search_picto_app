package com.searchpicto.ws.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.searchpicto.ws.exception.PictoNotFoundException;
import com.searchpicto.ws.model.Picto;
import com.searchpicto.ws.service.PictoService;

/**
 * Implementation of {@link PictoController}.
 * @author carol
 *
 */
@RestController
public class PictoControllerImpl implements PictoController {

	/**
	 * The Service associated to pictos.
	 */
	@Autowired
	private PictoService pictoService;

	@Override
	public Set<Picto> findPictosByTag(String tag) {
		Set<Picto> pictos = pictoService.findPictosByTagName(tag);
		if(pictos == null || pictos.isEmpty()) {
			throw new PictoNotFoundException(tag);
		}
		return pictos;
	}

	@Override
	public Picto getPictoById(Long id) {
		return pictoService.getPictoById(id).orElseThrow(() -> new PictoNotFoundException(id));
	}

}
