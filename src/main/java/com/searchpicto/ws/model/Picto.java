/**
 * 
 */
package com.searchpicto.ws.model;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * This class represents a Pictogram. A {@link Picto} is defined by a
 * {@link Media} and associated to various {@link Tag}.
 * 
 * @author carol
 *
 */
@Entity
@Table(name = "PICTOS")
public class Picto {

	/**
	 * The generated id.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long pictoId;

	/**
	 * The associated {@link Media}.
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "media_id", referencedColumnName = "mediaId")
	@JsonManagedReference
	private Media media;

	/**
	 * The {@link Tag} linked to the {@link Picto}.
	 */
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH })
	@JoinTable(name = "PICTOS_TAGS", joinColumns = @JoinColumn(name = "picto_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	@JsonManagedReference
	private Set<Tag> tags;

	/**
	 * The creation date of the {@link Picto}.
	 */
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime creationDate;

	/**
	 * Constructor.
	 */
	public Picto() {
		super();
	}

	/**
	 * creationDate getter.
	 *
	 * @return the creationDate.
	 */
	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	/**
	 * creationDate setter.
	 *
	 * @param creationDate : the creationDate to set.
	 */
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
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
	 * media getter.
	 *
	 * @return the media.
	 */
	public Media getMedia() {
		return media;
	}

	/**
	 * media setter.
	 *
	 * @param media : the media to set.
	 */
	public void setMedia(Media media) {
		this.media = media;
	}

	/**
	 * tags getter.
	 *
	 * @return the tags.
	 */
	public Set<Tag> getTags() {
		return tags;
	}

	/**
	 * tags setter.
	 *
	 * @param tags : the tags to set.
	 */
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	/**
	 * Add tags to the existing ones.
	 * @param tags The tags to add.
	 */
	public void addTags(Set<Tag> tags) {
		this.tags.addAll(tags);
	}

}
