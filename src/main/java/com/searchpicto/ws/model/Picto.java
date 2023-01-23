/**
 * 
 */
package com.searchpicto.ws.model;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="PICTOS")
public class Picto {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long pictoId;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "media_id", referencedColumnName = "mediaId")
	private Media media;
	
	@ManyToMany
	@JoinTable(name = "PICTOS_TAGS", 
			  joinColumns = @JoinColumn(name = "picto_id"), 
			  inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private Set<Tag> tags;
	
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
	
}
