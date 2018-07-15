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
@Table(name = "ChannelTypes", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "value", "sortId" }) })
@JsonInclude(Include.NON_DEFAULT)
public class ChannelType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5093252341685402124L;

	@Id
	@Column(nullable = false, length = 16)
	private String name = "";

	@Column(nullable = false, length = 9)
	private int value = 0;

	@Column(nullable = false, length = 4)
	private int sortId = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getSortId() {
		return sortId;
	}

	public void setSortId(int sortId) {
		this.sortId = sortId;
	}
}
