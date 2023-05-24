package com.searchpicto.ws.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import com.searchpicto.ws.model.Picto;

/**
 * Repository for pictos.
 * @author carol
 *
 */
public interface PictoRepository extends ListPagingAndSortingRepository<Picto, Long>, CrudRepository<Picto, Long> {
	
}
