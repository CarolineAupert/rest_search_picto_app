package com.searchpicto.ws.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.searchpicto.ws.dto.PictoDto;
import com.searchpicto.ws.exception.PictoIndexingException;
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
	 * The logger.
	 */
	Logger logger = LoggerFactory.getLogger(PictoControllerImpl.class);
	
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
	public List<PictoDto> findPictosByQuery(String query) throws PictoIndexingException {
		var pictos = pictoService.findPictosByQuery(query);
		if (pictos == null) {
			pictos = new ArrayList<>();
		}
		return pictos.stream().map(pictoMapper::pictoToPictoDto).collect(Collectors.toList());
	}

	@Override
	public PictoDto getPictoById(Long id) {
		Optional<Picto> picto = pictoService.getPictoById(id);
		if (picto.isPresent()) {
			return pictoMapper.pictoToPictoDto(picto.get());
		} else {
			logger.warn(String.format("PictoNotFoundException thrown for the id %d", id));
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
