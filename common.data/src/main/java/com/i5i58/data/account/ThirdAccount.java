package com.i5i58.data.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "ThirdAccounts", uniqueConstraints = { @UniqueConstraint(columnNames = { "thirdId" }) })
@JsonInclude(Include.NON_DEFAULT)
public class ThirdAccount implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6459037296458695937L;

	@Id
	@Column(nullable = false, length = 64)
	private String thirdId = "";
	
	@Column(nullable = false, length = 64)
	private String openId = "";
	
	@Column(nullable = false, length = 64)
	private String uId = "";

	@Column(nullable = false, length = 1)
	private int thirdType;

	@Column(nullable = false, length = 32)
	private String accId = "";

	@Column(nullable = false, length = 18)
	private long createTime;

	public String getThirdId() {
		return thirdId;
	}

	public void setThirdId(String thirdId) {
		this.thirdId = thirdId;
	}

	public int getThirdType() {
		return thirdType;
	}

	public void setThirdType(int thirdType) {
		this.thirdType = thirdType;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}
}
