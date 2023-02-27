package com.searchpicto.ws.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.searchpicto.ws.dto.PictoDto;
import com.searchpicto.ws.model.Media;
import com.searchpicto.ws.model.Picto;
import com.searchpicto.ws.model.Tag;

/**
 * Implementation of {@link PictoMapper}.
 * 
 * @author carol
 *
 */
@Component
public class PictoMapperImpl implements PictoMapper {

	@Override
	public PictoDto pictoToPictoDto(Picto picto) {
		PictoDto pictoTarget = new PictoDto();
		if(picto != null) {
			pictoTarget.setPictoId(picto.getPictoId());
			pictoTarget.setLocation(getLocation(picto.getMedia()));
			pictoTarget.setCreationDate(getCreationDateFormatted(picto.getCreationDate()));
			pictoTarget.setTags(getTagsNames(picto.getTags()));
		}
		return pictoTarget;
	}
	

	/**
	 * Retrieve the location of a Media.
	 * @param media : the media from which to get the location.
	 * @return The location.
	 */
	private String getLocation(Media media) {
		if (media != null) {
			return media.getLocation();
		}
		return null;
	}
	
	/**
	 * Convert a {@link LocalDateTime} to a String "YYY-MM-DD".
	 * 
	 * @param date The date to convert.
	 * @return The {@link String} formatted.
	 */
	private String getCreationDateFormatted(LocalDateTime date) {
		if (date != null) {
			return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
		}
		return null;
	}
	
	/**
	 * Retrieve the tags names from a {@link Set}of {@link Tag}.
	 *
	 * @param tags : the tags to set.
	 * @return The tags names.
	 */
	public Set<String> getTagsNames(Set<Tag> tags) {
		if(tags != null) {
			return tags.stream().map(Tag::getTagId).collect(Collectors.toSet());
		}
		return new HashSet<>();
	}

}
