package com.searchpicto.ws.repository;

import org.springframework.data.repository.ListCrudRepository;

import com.searchpicto.ws.model.Picto;

/**
 * Repository for pictos.
 * @author carol
 *
 */
public interface PictoRepository extends ListCrudRepository<Picto, Long> {
	
}
