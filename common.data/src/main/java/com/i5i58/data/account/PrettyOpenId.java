package com.i5i58.data.account;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "PrettyOpenIds", uniqueConstraints = { @UniqueConstraint(columnNames = { "openId" }) })
@JsonInclude(Include.NON_DEFAULT)
public class PrettyOpenId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5281545708285674696L;

	@Id
	@Column(nullable = false, length = 32)
	private String openId = "";

	@Column(nullable = false)
	private byte used;

	@Column(nullable = false)
	private long usedDate;

	@Column(nullable = false)
	private String type = "";

	@Column(nullable = false)
	private long price;

	private int nullity;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public byte getUsed() {
		return used;
	}

	public void setUsed(byte used) {
		this.used = used;
	}
	public long getUsedDate() {
		return usedDate;
	}

	public void setUsedDate(long usedDate) {
		this.usedDate = usedDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public int getNullity() {
		return nullity;
	}

	public void setNullity(int nullity) {
		this.nullity = nullity;
	}

}
