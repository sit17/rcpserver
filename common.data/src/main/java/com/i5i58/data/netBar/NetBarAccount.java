package com.i5i58.data.netBar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "NetBarAccounts")
@JsonInclude(Include.NON_DEFAULT)
public class NetBarAccount implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2153305266066189464L;

	@Id
	@Column(nullable = false, length = 32)
	private String nId = "";

	@Column(nullable = false, length = 32)
	private String name = "";

	@Column(nullable = false, length = 255)
	private String addr = "";

	@Column(nullable = false, length = 15, unique = true)
	private String netIp = "";

	@Column(nullable = false, length = 32)
	private String ownerId;

	private long createTime = 0;

	private boolean nullity = false;

	private String agId = "";
	
	private String lastAdmin="";
	
	private long lastAdminOnline=0;

	public String getnId() {
		return nId;
	}

	public void setnId(String nId) {
		this.nId = nId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
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

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public boolean isNullity() {
		return nullity;
	}

	public void setNullity(boolean nullity) {
		this.nullity = nullity;
	}

	public String getAgId() {
		return agId;
	}

	public void setAgId(String agId) {
		this.agId = agId;
	}

	public String getLastAdmin() {
		return lastAdmin;
	}

	public void setLastAdmin(String lastAdmin) {
		this.lastAdmin = lastAdmin;
	}

	public long getLastAdminOnline() {
		return lastAdminOnline;
	}

	public void setLastAdminOnline(long lastAdminOnline) {
		this.lastAdminOnline = lastAdminOnline;
	}
}
