package com.i5i58.data.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "BusinessAgent")
@JsonInclude(Include.NON_DEFAULT)
public class BusinessAgent implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4992264785391912015L;

	@Id
	@Column(nullable = false, length = 32)
	private String accId = "";

	@Column(nullable = false)
	private String openId = "";
	private String agentName = "";
	private String agentPhone = "";
	private String agentQQ = "";
	private long collectTime = 0;
	private boolean nullity = false;

	@Column(nullable = false)
	@JsonIgnore
	private int adminRight = 0;

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentPhone() {
		return agentPhone;
	}

	public void setAgentPhone(String agentPhone) {
		this.agentPhone = agentPhone;
	}

	public String getAgentQQ() {
		return agentQQ;
	}

	public void setAgentQQ(String agentQQ) {
		this.agentQQ = agentQQ;
	}

	public long getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(long collectTime) {
		this.collectTime = collectTime;
	}

	public boolean isNullity() {
		return nullity;
	}

	public void setNullity(boolean nullity) {
		this.nullity = nullity;
	}

	public int getAdminRight() {
		return adminRight;
	}

	public void setAdminRight(int adminRight) {
		this.adminRight = adminRight;
	}
}
