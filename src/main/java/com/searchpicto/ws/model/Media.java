package com.searchpicto.ws.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="MEDIAS")
public class Media {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long mediaId;
	
	private float weight;
	
	@Column(nullable=false)
	private String location;
	
	@OneToOne(mappedBy = "media")
	private Picto picto;
	
	// Todo mettre enum avec ce qui est accepté
	@Column(nullable=false)
	private String type;
	/**
	 * mediaId getter.
	 *
	 * @return the mediaId.
	 */
	public Long getMediaId() {
		return mediaId;
	}
	/**
	 * mediaId setter.
	 *
	 * @param mediaId : the mediaId to set.
	 */
	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}
	/**
	 * weight getter.
	 *
	 * @return the weight.
	 */
	public float getWeight() {
		return weight;
	}
	/**
	 * weight setter.
	 *
	 * @param weight : the weight to set.
	 */
	public void setWeight(float weight) {
		this.weight = weight;
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
	 * type getter.
	 *
	 * @return the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * type setter.
	 *
	 * @param type : the type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

}
