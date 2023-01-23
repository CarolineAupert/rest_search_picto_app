package com.searchpicto.ws.repository;

import org.springframework.data.repository.ListCrudRepository;

import com.searchpicto.ws.model.Picto;

public interface PictoRepository extends ListCrudRepository<Picto, Long> {
	
}
