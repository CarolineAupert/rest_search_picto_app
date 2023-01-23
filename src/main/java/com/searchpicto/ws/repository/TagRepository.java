package com.searchpicto.ws.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.searchpicto.ws.model.Tag;

@Component
public interface TagRepository extends CrudRepository<Tag, String> {
}
