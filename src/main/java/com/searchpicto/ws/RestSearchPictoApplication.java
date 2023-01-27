package com.searchpicto.ws;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import com.searchpicto.ws.model.Media;
import com.searchpicto.ws.model.Picto;
import com.searchpicto.ws.model.Tag;
import com.searchpicto.ws.service.PictoService;

@SpringBootApplication
public class RestSearchPictoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestSearchPictoApplication.class, args);
	}

	@Autowired
	private Environment environment;

	@Bean
	public CommandLineRunner demo(PictoService pictoService) {
		return args -> {
			if (!Arrays.asList(environment.getActiveProfiles()).contains("test")) {
				pictoService.addNewPicto(initPicto("Loupe.jpg",
						Stream.of("loupe", "détail", "chercher", "analyser", "zoom").collect(Collectors.toSet())));
				pictoService.addNewPicto(initPicto("Parchemin.jpg",
						Stream.of("parchemin", "détail", "contrat", "législation").collect(Collectors.toSet())));

			}
		};
	}

	private Picto initPicto(String location, Set<String> tags) {
		Picto picto = new Picto();
		picto.setCreationDate(LocalDateTime.now());
		picto.setMedia(new Media(location));
		picto.setTags(tags.stream().map(Tag::new).collect(Collectors.toSet()));
		return picto;
	}

}
