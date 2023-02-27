package com.searchpicto.ws.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/**
 * This class represents the various tags that could be associated to a {@link Picto}.
 * 
 * A {@link Picto}can be linked to several {@link Tag} and a {@link Tag}can be associated to numerous {@link Picto}.
 * 
 * @author carol
 *
 */
@Entity
@Table(name = "TAGS")
public class Tag {

	// TODO voir length
	/**
	 * The id, defined by the tag value itself.
	 */
	@Id
	private String tagId;

	/**
	 * The {@link Picto} associated to this {@link Tag}.
	 */
	@ManyToMany(mappedBy = "tags")
	@JsonBackReference
	private Set<Picto> pictos;
	
	/**
	 * Default constructor.
	 */
	public Tag() {
		super();
	}

	/**
	 * Constructor with id.
	 * @param tagId The tag id.
	 */
	public Tag(String tagId) {
		super();
		this.tagId = tagId;
	}

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
