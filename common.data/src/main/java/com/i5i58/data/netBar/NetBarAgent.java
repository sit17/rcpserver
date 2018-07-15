package com.i5i58.data.netBar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "NetBarAgents")
@JsonInclude(Include.NON_DEFAULT)
public class NetBarAgent implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -62166549009124376L;

	@Id
	@Column(nullable = false, length = 32)
	private String agId = "";

	@Column(nullable = false, length = 32)
	private String name = "";

	@Column(nullable = false, length = 255)
	private String area = "";

	@Column(nullable = false, length = 32, unique = true)
	private String ownerId;

	private long createTime = 0;

	private boolean nullity = false;

	public String getAgId() {
		return agId;
	}

	public void setAgId(String agId) {
		this.agId = agId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public boolean isNullity() {
		return nullity;
	}

	public void setNullity(boolean nullity) {
		this.nullity = nullity;
	}
}
