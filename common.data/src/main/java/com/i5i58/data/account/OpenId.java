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
@Table(name = "OpenIds", uniqueConstraints = { @UniqueConstraint(columnNames = { "open_id" }) })
// @NamedNativeQuery(name = "findOneOpenIdWithRandom",
// query = "SELECT openIds_1.open_id, openIds_1.used from OpenIds openIds_1 by
// rand() limit 1",
// resultClass = OpenId.class)
// @NamedQueries({
// @NamedQuery(name = "findOneOpenIdWithRandom", query = "SELECT randOpenId from
// randOpenId openId order by rand() limit 1")})
@JsonInclude(Include.NON_DEFAULT)
public class OpenId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7870230866392842267L;

	@Id
	@Column(name = "open_id", length = 32)
	private String openId = "";

	@Column(name = "used", nullable = false)
	private byte used;

	@Column(name = "used_date", nullable = false)
	private long usedDate;

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

}
