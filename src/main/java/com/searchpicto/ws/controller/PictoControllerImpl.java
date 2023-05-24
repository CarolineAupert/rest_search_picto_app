package com.searchpicto.ws.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.searchpicto.ws.dto.PictoDto;
import com.searchpicto.ws.exception.PictoNotFoundException;
import com.searchpicto.ws.mapper.PictoMapper;
import com.searchpicto.ws.model.Picto;
import com.searchpicto.ws.service.PictoService;

/**
 * Implementation of {@link PictoController}.
 * 
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

	/**
	 * The model mapper.
	 */
	@Autowired
	private PictoMapper pictoMapper;

	@Override
	public Set<PictoDto> findPictosByTag(String tag) {
		var pictos = pictoService.findPictosByTagName(tag);
		if (pictos == null) {
			pictos = new HashSet<>();
		}
		return pictos.stream().map(pictoMapper::pictoToPictoDto).collect(Collectors.toSet());
	}

	@Override
	public PictoDto getPictoById(Long id) {
		Optional<Picto> picto = pictoService.getPictoById(id);
		if (picto.isPresent()) {
			return pictoMapper.pictoToPictoDto(picto.get());
		} else {
			throw new PictoNotFoundException(id);
		}
	}

	@Override
	public List<PictoDto> getLastPictosAdded(int sizeLimit) {
		var pictos = pictoService.getLastPictosAdded(sizeLimit);
		if (pictos == null) {
			pictos = new ArrayList<>();
		}
		return pictos.stream().map(pictoMapper::pictoToPictoDto).collect(Collectors.toList());

	}
}
