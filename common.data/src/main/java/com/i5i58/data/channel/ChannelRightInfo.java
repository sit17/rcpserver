package com.i5i58.data.channel;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "ChannelRightInfos", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
@JsonInclude(Include.NON_DEFAULT)
public class ChannelRightInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7531540161957504936L;

	@Id
	private int id;

	private String name = "";

	private String imgUrl = "";

	private String action = "";

	private String target = "";

	private String params = "";

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

}
