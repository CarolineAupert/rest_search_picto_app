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

	
	// Will be removed
	@Bean
	CommandLineRunner demo(PictoService pictoService) {
		return args -> {
			pictoService.addNewPicto(initPicto("https://caukaro.fr/wp-content/uploads/2023/02/Loupe.jpg","Une loupe",
					Stream.of("loupe", "détail", "chercher", "analyser", "zoom", "picto").collect(Collectors.toSet())));
			pictoService.addNewPicto(initPicto("https://caukaro.fr/wp-content/uploads/2023/02/Parchemin.jpg","Un parchemin",
					Stream.of("parchemin", "détail", "contrat", "législation", "picto").collect(Collectors.toSet())));
			pictoService.addNewPicto(initPicto("https://caukaro.fr/wp-content/uploads/2023/02/Travail-ordi.jpg","Un perso avec son ordi",
					Stream.of("ordinateur", "travail", "télétravail", "heureux", "picto", "perso").collect(Collectors.toSet())));
			pictoService.addNewPicto(initPicto("https://caukaro.fr/wp-content/uploads/2023/02/Tirelire.jpg","Une tirelire cochon",
					Stream.of("tirelire", "cochon", "argent", "picto").collect(Collectors.toSet())));
			pictoService.addNewPicto(initPicto("https://caukaro.fr/wp-content/uploads/2023/02/Surf-plante.jpg","Une feuille pour planche de surf",
					Stream.of("surf", "écologie", "plante", "picto", "perso").collect(Collectors.toSet())));
			pictoService.addNewPicto(initPicto("https://caukaro.fr/wp-content/uploads/2023/02/Questionnement.jpg","Une question",
					Stream.of("question", "perso", "picto", "réfléchir").collect(Collectors.toSet())));
		};
	}

	private Picto initPicto(String location, String title, Set<String> tags) {
		Picto picto = new Picto();
		picto.setCreationDate(LocalDateTime.now());
		picto.setMedia(new Media(location, title));
		picto.setTags(tags.stream().map(Tag::new).collect(Collectors.toSet()));
		return picto;
	}

}
