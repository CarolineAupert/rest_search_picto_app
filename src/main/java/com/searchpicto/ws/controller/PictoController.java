package com.searchpicto.ws.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.searchpicto.ws.dto.PictoDto;
import com.searchpicto.ws.model.Picto;

/**
 * Controller class for pictograms.
 * 
 * @author carol
 *
 */
public interface PictoController {

	/**
	 * Retrieve all the pictos associated to a tag.
	 * @param tag The tag searched.
	 * @return The pictos associated.
	 */
	// CrossOrigin used for local tests, will be removed soon
	@CrossOrigin
	@GetMapping("/pictos")
	public Set<PictoDto> findPictosByTag(@RequestParam(value = "tag") String tag);

	/**
	 * Retrieve a {@link Picto} from its id.
	 * @param id The Picto id.
	 * @return The picto wanted.
	 */
	@GetMapping("/picto")
	public PictoDto getPictoById(@RequestParam(value = "id") Long id);

}
