package com.i5i58.data.social;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RedisHash("HotSongs")
@JsonInclude(Include.NON_DEFAULT)
public class HotSongs implements java.io.Serializable {

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
	private String playCount = "";
	private String shareCount = "";
	private String uploadDate = "";
	private String uploadTime = "";
	private String uuId = "";

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

	public String getFilmUrl() {
		return filmUrl;
	}

	public void setFilmUrl(String filmUrl) {
		this.filmUrl = filmUrl;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPlayCount() {
		return playCount;
	}

	public void setPlayCount(String playCount) {
		this.playCount = playCount;
	}

	public String getShareCount() {
		return shareCount;
	}

	public void setShareCount(String shareCount) {
		this.shareCount = shareCount;
	}

	public String getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getUuId() {
		return uuId;
	}

	public void setUuId(String uuId) {
		this.uuId = uuId;
	}

}
