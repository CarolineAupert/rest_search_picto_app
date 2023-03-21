package com.searchpicto.ws.dto;

import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.core.style.ToStringCreator;

/**
 * The Picto data that will be transferred to the client.
 * 
 * @author carol
 *
 */
/**
 * @author carol
 *
 */
/**
 * @author carol
 *
 */
public class PictoDto {

	/**
	 * The id.
	 */
	private Long pictoId;

	/**
	 * The media mocation.
	 */
	private String location;

	/**
	 * The tags linked to the picto.
	 */
	private Set<String> tags;

	/**
	 * The creation date of the picto.
	 */
	private String creationDate;
	
	
	/**
	 * The media title. 
	 */
	private String title;
	
	

	/**
	 * Empty constructor.
	 */
	public PictoDto() {
		super();
	}

	/**
	 * Constructor with fields.
	 * @param pictoId Picto ID,
	 * @param location Media location,
	 * @param tags Tags names,
	 * @param creationDate Creation Date.
	 */
	public PictoDto(Long pictoId, String location, Set<String> tags, String creationDate, String title) {
		super();
		this.pictoId = pictoId;
		this.location = location;
		this.tags = tags;
		this.creationDate = creationDate;
		this.title = title;
	}

	
	/**
	 * title getter.
	 *
	 * @return the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * title setter.
	 *
	 * @param title : the title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * pictoId getter.
	 *
	 * @return the pictoId.
	 */
	public Long getPictoId() {
		return pictoId;
	}

	/**
	 * pictoId setter.
	 *
	 * @param pictoId : the pictoId to set.
	 */
	public void setPictoId(Long pictoId) {
		this.pictoId = pictoId;
	}

	/**
	 * location getter.
	 *
	 * @return the location.
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * location setter.
	 *
	 * @param location : the location to set.
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * tags getter.
	 *
	 * @return the tags.
	 */
	public Set<String> getTags() {
		return tags;
	}

	/**
	 * tags setter.
	 *
	 * @param tags : the tags to set.
	 */
	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	/**
	 * creationDate getter.
	 *
	 * @return the creationDate.
	 */
	public String getCreationDate() {
		return creationDate;
	}

	/**
	 * creationDate setter.
	 *
	 * @param creationDate : the creationDate to set.
	 */
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	
	@Override
	public String toString() {
		return new ToStringCreator(this)
		        .append("pictoId", this.getPictoId())
		        .append("location", this.getLocation())
		        .append("title", this.getTitle())
		        .append("creationDate", this.getCreationDate())
		        .append("tags", this.getTags())
		        .toString();
	}
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

}
