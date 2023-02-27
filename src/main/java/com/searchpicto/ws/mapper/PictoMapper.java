package com.searchpicto.ws.mapper;

import org.springframework.stereotype.Component;

import com.searchpicto.ws.dto.PictoDto;
import com.searchpicto.ws.model.Picto;

/**
 * This class is a utility class to convert {@link Picto}.
 * 
 * @author carol
 *
 */
@Component
public interface PictoMapper {
	
	public PictoDto pictoToPictoDto(Picto picto);

}
