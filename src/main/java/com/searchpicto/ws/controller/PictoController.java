package com.searchpicto.ws.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.searchpicto.ws.model.Picto;

public interface PictoController {
	
	@GetMapping("/pictos")
	Set<Picto> findPictosByTag(@RequestParam(value="tag") String tag);
	
	@GetMapping("/picto")
	Picto getPictoById(@RequestParam(value="id")Long id);
}