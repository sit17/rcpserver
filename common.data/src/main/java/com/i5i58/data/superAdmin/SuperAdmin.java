package com.i5i58.data.superAdmin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "SuperAdmins", uniqueConstraints = { @UniqueConstraint(columnNames = "accId") })
@JsonInclude(Include.NON_DEFAULT)
public class SuperAdmin implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6228027909276582221L;

	@Id
	@Column(nullable = false, length = 32)
	private String accId = "";

	/**
	 * 个人基本信息
	 */
	@Column(nullable = false)
	private String openId = "";
	private String phoneNo = "";
	private String emailAddress = "";
	private int adminRight;

	@JsonIgnore
	private String password = "";

	private String realName = "";
	private String depart = "";
	private byte gender;
	private long birthDate;
	private String location = "";
	private boolean nullity;

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

	public int getAdminRight() {
		return adminRight;
	}

	public void setAdminRight(int adminRight) {
		this.adminRight = adminRight;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getDepart() {
		return depart;
	}

	public void setDepart(String depart) {
		this.depart = depart;
	}

	public byte getGender() {
		return gender;
	}

	public void setGender(byte gender) {
		this.gender = gender;
	}

	public long getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(long birthDate) {
		this.birthDate = birthDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isNullity() {
		return nullity;
	}

	public void setNullity(boolean nullity) {
		this.nullity = nullity;
	}

}
