package com.i5i58.data.netBar;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "NetBarAdmins")
@JsonInclude(Include.NON_DEFAULT)
public class NetBarAdmin implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8789433738877984716L;

	@Id
	@Column(nullable = false, length = 32)
	private String accId = "";

	@Column(nullable = false, length = 32)
	private String name = "";

	@Column(nullable = false, length = 15)
	private String netIp = "";

	private long createTime = 0;

	private boolean nullity = false;


	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNetIp() {
		return netIp;
	}

	public void setNetIp(String netIp) {
		this.netIp = netIp;
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
