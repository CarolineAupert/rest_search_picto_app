package com.searchpicto.ws.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Component;

import com.searchpicto.ws.model.Picto;

/**
 * Repository for searching pictos.
 * @author carol
 *
 */
@Component
public interface PictoRepository extends ListPagingAndSortingRepository<Picto, Long>, CrudRepository<Picto, Long> {
}
