package com.searchpicto.ws.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * This class represents a Media, that is to say the image of the {@link Picto}.
 * 
 * A media is uploaded by a user and converted in WebP to be as light as possible.
 * 
 * @author carol
 *
 */
/**
 * @author carol
 *
 */
@Entity
@Table(name="MEDIAS")
public class Media {
	
	/**
	 * The generated ID of the {@link Media}
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long mediaId;
	
	/**
	 * The weight of the image before WebP conversion.
	 * This value is useful only for {@link Picto} validation, that's why it is not stored in DB.
	 */
	@Transient
	private float weight;
	
	/**
	 * The location of the image.
	 */
	@Column(nullable=false)
	private String location;
	
	/**
	 * The {@link Picto} associated to the Media. 
	 */
	@OneToOne(mappedBy = "media")
	@JsonBackReference
	private Picto picto;
	
	/**
	 * The type of the image before WebP conversion.
	 * This value is useful only for {@link Picto} validation, that's why it is not stored in DB.
	 */
	@Transient
	private MediaType type;
	
	/**
	 * The media title
	 */
	@Column(nullable=false)
	private String title;
	
	/**
	 * Default constructor.
	 */
	public Media() {
		super();
	}
	
	/**
	 * Constructor with an image only.
	 * @param location The image location.
	 * @param title The image title.
	 */
	public Media(String location, String title) {
		super();
		this.location = location;
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
	public MediaType getType() {
		return type;
	}
	/**
	 * type setter.
	 *
	 * @param type : the type to set.
	 */
	public void setType(MediaType type) {
		this.type = type;
	}

}
