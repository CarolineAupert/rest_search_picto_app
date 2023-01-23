package com.searchpicto.ws.model;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "TAGS")
public class Tag {

	// TODO voir length
	@Id
	private String tagId;

	@ManyToMany(mappedBy = "tags")
	private Set<Picto> pictos;

	/**
	 * tagId getter.
	 *
	 * @return the tagId.
	 */
	public String getTagId() {
		return tagId;
	}

	/**
	 * tagId setter.
	 *
	 * @param tagId : the tagId to set.
	 */
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	/**
	 * pictos getter.
	 *
	 * @return the pictos.
	 */
	public Set<Picto> getPictos() {
		return pictos;
	}

	/**
	 * pictos setter.
	 *
	 * @param pictos : the pictos to set.
	 */
	public void setPictos(Set<Picto> pictos) {
		this.pictos = pictos;
	}

}
