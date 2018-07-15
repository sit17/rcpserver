package com.i5i58.data.channel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "ChannelSound", uniqueConstraints = { @UniqueConstraint(columnNames = "guid") })
@JsonInclude(Include.NON_DEFAULT)
public class ChannelSound implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable = false, length = 32)
	private String guid = "";

	@Column(nullable = false)
	private String name = "";
	private String path = "";
	
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
