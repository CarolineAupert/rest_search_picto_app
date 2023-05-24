package com.searchpicto.ws.controller;

import java.util.List;
import java.util.Set;

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
	 * 
	 * @param tag The tag searched.
	 * @return The pictos associated.
	 */
	@GetMapping(value = "/pictos", params = "tag")
	Set<PictoDto> findPictosByTag(@RequestParam(value = "tag") String tag);

	/**
	 * Retrieve a {@link Picto} from its id.
	 * 
	 * @param id The Picto id.
	 * @return The picto wanted.
	 */
	@GetMapping("/picto")
	PictoDto getPictoById(@RequestParam(value = "id") Long id);

	/**
	 * Retrieve all the n last pictos added.
	 * 
	 * @param sizeLimit The number of pictos to retrieve.
	 * @return The n pictos found.
	 */
	@GetMapping(value = "/pictos", params = "last")
	List<PictoDto> getLastPictosAdded(@RequestParam(value = "last") int sizeLimit);

}
