package com.searchpicto.ws;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	/**
	 * The logger.
	 */
	Logger logger = LoggerFactory.getLogger(RestSearchPictoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RestSearchPictoApplication.class, args);
	}

	/**
	 * The environment.
	 */
	@Autowired
	private Environment environment;

	/**
	 * Initializes the index. This will be changed when the database get bigger.
	 * 
	 * @param pictoService Picto service.
	 * @return CommandLineRunner
	 */
	@Bean
	CommandLineRunner run(PictoService pictoService) {
		return args -> {
			if (!Arrays.asList(environment.getActiveProfiles()).contains("test")) {
				logger.info("----- Indexing pictos : start -----");
				pictoService.indexAllPictos();
				logger.info("----- Indexing pictos : done -----");
			}

		};
	}

}
