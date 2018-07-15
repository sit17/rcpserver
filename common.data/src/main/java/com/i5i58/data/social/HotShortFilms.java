package com.i5i58.data.social;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RedisHash("HotShortFilms")
@JsonInclude(Include.NON_DEFAULT)
public class HotShortFilms implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6087741958323462628L;

	@Id
	@JsonIgnore
	private String id = "";

	@Indexed
	private String accId = "";
	@Indexed
	private String cover = "";
	private String filmUrl = "";
	private String icon = "";
	private String likeCount = "";
	private Long uploadTime = 0L;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(String likeCount) {
		this.likeCount = likeCount;
	}

	public String getFilmUrl() {
		return filmUrl;
	}

	public void setFilmUrl(String filmUrl) {
		this.filmUrl = filmUrl;
	}

	public Long getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Long uploadTime) {
		this.uploadTime = uploadTime;
	}

}
