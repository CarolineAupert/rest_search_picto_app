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
	
	@Autowired
	private Environment environment;

	// Will be removed
	@Bean
	CommandLineRunner demo(PictoService pictoService) {
		logger.info(String.format("Application runs in %s environment.",Arrays.stream(environment.getActiveProfiles()).toString()));
		
		return args -> {
			if (!Arrays.asList(environment.getActiveProfiles()).contains("test")) {

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Appuyer-bouton.jpg",
								"Appuyer sur un bouton",
								Stream.of("bouton", "déclencher", "départ", "alarme", "doigt", "appuyer")
										.collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Auto-portrait.jpg",
								"Auto-portrait",
								Stream.of("auto", "portrait", "dessiner", "personnage").collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Bescher.jpg",
								"Bescher", Stream.of("bescher", "expérience", "test", "bulles", "chimie", "potion")
										.collect(Collectors.toSet())));

				pictoService.addNewPicto(initPicto(
						"https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Bourse-argent.jpg", "Bourse",
						Stream.of("bourse", "argent", "trésor", "monnaie", "économies", "budget", "billets", "pièces",
								"acheter", "achat", "euros", "dollars", "économie", "investissement", "dépense")
								.collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Bulles-parole.jpg",
								"Bulles de paroles", Stream.of("bulles", "communication", "parler", "dialogue", "débat",
										"parole", "parler", "arguments").collect(Collectors.toSet())));

				pictoService.addNewPicto(initPicto(
						"https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Caddie.jpg", "Caddie",
						Stream.of("caddie", "chariot", "achat", "courses", "shopping", "magasin", "dépenses")
								.collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Cadenas.jpg",
								"Cadenas", Stream.of("cadenas", "verrou", "bloquer", "blocage", "interdit", "clef",
										"stop", "serrure", "fermer").collect(Collectors.toSet())));

				pictoService.addNewPicto(initPicto(
						"https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Clef.jpg", "Clef",
						Stream.of("clef", "clé", "solution", "débloquer", "ouvrir", "déverrouiller", "porte", "serrure")
								.collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Cone-travaux.jpg",
								"Cône de travaux", Stream
										.of("travaux", "cône", "en cours", "chantier", "attention", "danger",
												"sécurité", "balise", "avertir", "construction")
										.collect(Collectors.toSet())));

				pictoService.addNewPicto(initPicto(
						"https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Content-travail.jpg",
						"Présenter feuille", Stream.of("présenter", "heureux", "montrer", "personnage", "content",
								"fier", "feuille", "inspiration", "fini").collect(Collectors.toSet())));

				pictoService.addNewPicto(initPicto(
						"https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Courir-engrenages.jpg",
						"Courir sur des engrenages", Stream.of("courir", "engrenages", "fonctionnement", "travail",
								"difficulté", "réfléchir", "mécanisme", "analyse").collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Eclair-equipes.jpg",
								"Eclair entre équipes",
								Stream.of("éclair", "conflit", "équipes", "dispute", "orage", "malentendu")
										.collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Ecrire.jpg",
								"Ecrire dans un cahier",
								Stream.of("écrire", "cahier", "heureux", "inspiration", "livre", "journal", "rédiger",
										"travailler", "réfléchir", "auteur", "écrivain", "dveloppement")
										.collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Equipe-clef.jpg",
								"Equipe portant une clef", Stream
										.of("clef", "collectif", "synergie", "solution", "travail", "équipe",
												"apporter", "présenter", "groupe", "collaboration")
										.collect(Collectors.toSet())));

				pictoService.addNewPicto(initPicto(
						"https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Equipe-engrenage.jpg",
						"Equipe sur un engrenage",
						Stream.of("engrenage", "fonctionnement", "équipe", "organisation", "groupe", "développement")
								.collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Equipe-tableau.jpg",
								"Equipe face à un tableau",
								Stream.of("équipe", "tableau", "apprentissage", "formation", "présentation", "assister",
										"travail", "atelier", "paperboard", "groupe", "classe")
										.collect(Collectors.toSet())));

				pictoService.addNewPicto(initPicto(
						"https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Equipe.jpg", "Equipe",
						Stream.of("Equipe", "groupe", "personnes", "gens").collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Idee-engrenage.jpg",
								"Idée engrenage",
								Stream.of("idée", "inspiration", "réflexion", "réfléchir", "personnage", "déclic",
										"engrenage", "solution", "montrer", "heureux", "content", "résoudre",
										"résolution", "analyse", "raisonnement").collect(Collectors.toSet())));

				pictoService.addNewPicto(initPicto(
						"https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Livre-objectif.jpg",
						"Livre objectif",
						Stream.of("livre", "objectif", "but", "cahier", "apprentissage", "flèche", "planté", "cible")
								.collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Loupe.jpg", "Loupe",
								Stream.of("loupe", "chercher", "grossir", "détail", "analyse", "zoom", "comprendre")
										.collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Mecanicien.jpg",
								"Mecanicien", Stream
										.of("mécanicien", "réparateur", "outil", "solution", "engrenages", "analyse",
												"concret", "lunettes", "réfléchir", "perso")
										.collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Medecin.jpg",
								"Médecins", Stream.of("médecin", "blouse", "perso", "infirmier", "hôpital", "soigner")
										.collect(Collectors.toSet())));

				pictoService.addNewPicto(initPicto(
						"https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Monter-marches.jpg",
						"Monter des marches",
						Stream.of("perso", "collaboration", "aide", "collectif", "marche", "surmonter", "difficulté",
								"main", "grimper", "évoluer", "tirer", "accompagner", "développement")
								.collect(Collectors.toSet())));

				pictoService.addNewPicto(initPicto(
						"https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Ordi-cloud.jpg",
						"Ordinateur nuage",
						Stream.of("ordinateur", "pc", "nuage", "cloud", "dématérialisé", "technologie", "digital",
								"électronique", "écran", "travail", "télétravail", "sauvegarde", "data", "données")
								.collect(Collectors.toSet())));

				pictoService.addNewPicto(initPicto(
						"https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Panneau-interrogation.jpg",
						"Panneau interrogation",
						Stream.of("perso", "tenir", "panneau", "question", "caché", "interrogation", "réflexion",
								"inconnu", "incertitude", "perdu").collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Panneaux.jpg",
								"Panneaux", Stream.of("Panneau", "direction", "choix", "incertitude", "inconnu",
										"binaire", "chemin", "signe").collect(Collectors.toSet())));

				pictoService
						.addNewPicto(
								initPicto(
										"https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Parchemin.jpg",
										"Parchemin", Stream
												.of("parchemin", "papier", "rouleau", "feuille", "règles", "loi",
														"légal", "détails", "contrat", "législation")
												.collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Perdu-carte.jpg",
								"Perdu carte", Stream.of("perso", "perdu", "carte", "question", "interrogation", "lire",
										"géographie", "tenir", "aller", "chasse").collect(Collectors.toSet())));

				pictoService.addNewPicto(initPicto(
						"https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Personne-drapeau.jpg",
						"Tenir un drapeau", Stream.of("drapeau", "perso", "guide", "référent", "tenir", "coucou",
								"content", "heureux", "exemple").collect(Collectors.toSet())));

				pictoService.addNewPicto(initPicto(
						"https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Personnes-engrenages.jpg",
						"Personnes dans des engrenages",
						Stream.of("perso", "courir", "engrenages", "travail", "équipe", "groupe", "fonctionnement",
								"enttreprise", "rouages", "difficile", "tourner", "système", "analyse", "raisonnement")
								.collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Pile.jpg", "Pile",
								Stream.of("pile", "batterie", "énergie", "vie", "électricité", "éclair")
										.collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Pin-lieu.jpg",
								"Lieu", Stream.of("lieu", "endroit", "pointer", "ici", "geographie", "carte", "où",
										"pointeur", "objectif").collect(Collectors.toSet())));

				pictoService.addNewPicto(initPicto(
						"https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Plante-oreille.jpg",
						"Oreille sur une plante", Stream.of("nature", "plante", "oreille", "écoute", "harmonie",
								"pousser", "feuilles", "environnement", "permaculture").collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Porte-monnaie.jpg",
								"Porte-monnaie",
								Stream.of("porte-monnaie", "argent", "budget", "achat", "pièces", "monnaie", "euros",
										"acheter", "économie", "trésor", "investissement", "dépense")
										.collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Portrait-fleur.jpg",
								"Déguisé en fleur",
								Stream.of("perso", "panneau", "imiter", "fleur", "déguisé", "déguisement", "tenir",
										"tête", "pétales", "harmonie", "caché", "content", "heureux")
										.collect(Collectors.toSet())));

				pictoService.addNewPicto(initPicto(
						"https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Poubelle.jpg", "Poubelle",
						Stream.of("poubelle", "jeter", "inutile", "déchet", "recycler", "corbeille", "ordure")
								.collect(Collectors.toSet())));

				pictoService.addNewPicto(initPicto(
						"https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Questionnement.jpg",
						"Questionnement",
						Stream.of("perso", "réfléchir", "perdu", "question", "interrogation", "difficile", "dur")
								.collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Reflechir.jpg",
								"Réfléchir",
								Stream.of("réfléchir", "cerveau", "tête", "engrenage", "trouver", "stratégie", "courir",
										"travail", "dur", "difficile", "analyse", "raisonnement")
										.collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Requin.jpg",
								"Aileron de requin", Stream.of("aileron", "requin", "difficulté", "danger", "caché",
										"mer", "eau", "problème", "alerte").collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Serrage-main.jpg",
								"Serrer la main",
								Stream.of("mains", "serrer", "accord", "collaboration", "entraide", "aide", "contrat",
										"collectif", "synergie", "rencontre", "bonjour", "présentation", "poli",
										"respect", "fair-play").collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Surf-plante.jpg",
								"Surf sur une plante", Stream.of("surf", "plante", "nature", "écologie", "feuille",
										"transport", "propre", "permaculture").collect(Collectors.toSet())));

				pictoService
						.addNewPicto(
								initPicto(
										"https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Tirelire.jpg",
										"Tirelire", Stream
												.of("tirelire", "investissement", "pièces", "argent", "cochon", "euro",
														"dollar", "achat", "dépense", "économie")
												.collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Travail-ordi.jpg",
								"Assis avec ordinateur", Stream.of("perso", "content", "heureux", "assis", "ordinateur",
										"pc", "travail", "télétravail", "développement").collect(Collectors.toSet())));

				pictoService.addNewPicto(
						initPicto("https://picto-sketchnote-data.s3.eu-west-3.amazonaws.com/pictos/Video-dessinee.jpg",
								"Vidéo dessinée", Stream.of("vidéo", "dessiné", "créer").collect(Collectors.toSet())));

			}
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
