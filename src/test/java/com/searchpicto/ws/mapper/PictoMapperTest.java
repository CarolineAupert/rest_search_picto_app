package com.searchpicto.ws.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.searchpicto.ws.dto.PictoDto;
import com.searchpicto.ws.model.Media;
import com.searchpicto.ws.model.Picto;
import com.searchpicto.ws.model.Tag;

/**
 * Tests the class {@link PictoMapper}.
 * 
 * @author carol
 *
 */
class PictoMapperTest {

	/**
	 * The mapper to test.
	 */
	private PictoMapper mapper = new PictoMapperImpl();

	/**
	 * Method to init a Picto with Tags.
	 * 
	 * @param id       The Picto id.
	 * @param location The Media location.
	 * @param title    The Media title.
	 * @param tags     The Picto Tags.
	 * @return The created Picto.
	 */
	private Picto initPicto(Long id, String location, String title, Set<String> tags, LocalDateTime date) {
		Picto picto = new Picto();
		picto.setPictoId(id);
		picto.setMedia(new Media(location, title));
		picto.setTags(tags.stream().map(Tag::new).collect(Collectors.toSet()));
		picto.setCreationDate(date);
		return picto;
	}

	/**
	 * Tests pictoToPictoDto with a complete Picto.
	 */
	@Test
	void pictoToPictoDto_fullPicto_ok() throws Exception {
		// Values
		Long pictoId = Long.valueOf(5);
		String location = "Parchemin.jpg";
		String title = "Un parchemin";
		Set<String> tagsNames = Stream.of("parchemin", "detail", "contrat", "legislation").collect(Collectors.toSet());
		LocalDateTime creationDate = LocalDateTime.of(1990, 06, 04, 10, 12);

		// Test
		Picto pictoToConvert = initPicto(pictoId, location, title, tagsNames, creationDate);
		PictoDto pictoConverted = new PictoDto(pictoId, location, tagsNames, "1990-06-04", title);
		assertEquals(mapper.pictoToPictoDto(pictoToConvert), pictoConverted);
	}

	/**
	 * Tests pictoToPictoDto when the Picto does not have a Media.
	 */
	@Test
	void pictoToPictoDto_mediaNull_ok() throws Exception {
		// Values
		Long pictoId = Long.valueOf(5);
		String location = "Parchemin.jpg";
		String title = "Un parchemin";
		Set<String> tagsNames = Stream.of("parchemin", "detail", "contrat", "legislation").collect(Collectors.toSet());
		LocalDateTime creationDate = LocalDateTime.of(1990, 06, 04, 10, 12);

		// Test
		Picto pictoToConvert = initPicto(pictoId, location, title, tagsNames, creationDate);
		pictoToConvert.setMedia(null);
		PictoDto pictoConverted = new PictoDto(pictoId, null, tagsNames, "1990-06-04", null);
		assertEquals(mapper.pictoToPictoDto(pictoToConvert), pictoConverted);
	}

	/**
	 * Tests pictoToPictoDto when the Picto does not have a creation date.
	 */
	@Test
	void pictoToPictoDto_dateNull_ok() throws Exception {
		// Values
		Long pictoId = Long.valueOf(5);
		String location = "Parchemin.jpg";
		String title = "Un parchemin";
		Set<String> tagsNames = Stream.of("parchemin", "detail", "contrat", "legislation").collect(Collectors.toSet());

		// Test
		Picto pictoToConvert = initPicto(pictoId, location, title, tagsNames, null);
		PictoDto pictoConverted = new PictoDto(pictoId, location, tagsNames, null, title);
		assertEquals(mapper.pictoToPictoDto(pictoToConvert), pictoConverted);
	}

	/**
	 * Tests pictoToPictoDto when the Picto does not have tags.
	 */
	@Test
	void pictoToPictoDto_tagsNull_ok() throws Exception {
		// Values
		Long pictoId = Long.valueOf(5);
		String location = "Parchemin.jpg";
		String title = "Un parchemin";
		Set<String> tagsNames = Stream.of("parchemin", "detail", "contrat", "legislation").collect(Collectors.toSet());
		LocalDateTime creationDate = LocalDateTime.of(1990, 06, 04, 10, 12);

		// Test
		Picto pictoToConvert = initPicto(pictoId, location, title, tagsNames, creationDate);
		pictoToConvert.setTags(null);
		PictoDto pictoConverted = new PictoDto(pictoId, location, new HashSet<>(), "1990-06-04", title);
		assertEquals(mapper.pictoToPictoDto(pictoToConvert), pictoConverted);
	}

	/**
	 * Tests pictoToPictoDto when the Picto does not have a creation date nor an id.
	 */
	@Test
	void pictoToPictoDto_dateAndIdNull_ok() throws Exception {
		// Values
		Long pictoId = null;
		String location = "Parchemin.jpg";
		String title = "Un parchemin";
		Set<String> tagsNames = Stream.of("parchemin", "detail", "contrat", "legislation").collect(Collectors.toSet());

		// Test
		Picto pictoToConvert = initPicto(pictoId, location, title, tagsNames, null);
		PictoDto pictoConverted = new PictoDto(pictoId, location, tagsNames, null, title);
		assertEquals(mapper.pictoToPictoDto(pictoToConvert), pictoConverted);
	}

	/**
	 * Tests pictoToPictoDto when the Picto is null..
	 */
	@Test
	void pictoToPictoDto_pictoNull_ok() throws Exception {
		assertEquals(mapper.pictoToPictoDto(null), new PictoDto());
	}
}
