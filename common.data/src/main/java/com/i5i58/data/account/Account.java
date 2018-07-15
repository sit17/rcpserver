package com.i5i58.data.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "Accounts") // , uniqueConstraints = {
// @UniqueConstraint(columnNames = { "openId", "accId", "phoneNo",
// "emailAddress" }) })
@JsonInclude(Include.NON_DEFAULT)
public class Account implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2490999200268650863L;

	@Id
	@Column(nullable = false, length = 32)
	private String accId = "";

	/**
	 * 个人基本信息
	 */
	@Column(nullable = false, length = 32, unique = true)
	private String openId;

	@Column(nullable = true, length = 15, unique = true)
	private String phoneNo;

	@Column(nullable = true, length = 64, unique = true)
	private String emailAddress;

	@Column(nullable = false, length = 32)
	@JsonIgnore
	private String password = "";

	@Column(nullable = false, length = 32)
	private String nickName = "";

	@Column(nullable = true, length = 255)
	private String faceSmallUrl = "";

	@Column(nullable = true, length = 255)
	private String faceOrgUrl = "";

	@Column(nullable = true, length = 32)
	private String stageName = "";

	@Column(nullable = false, length = 1)
	private byte gender = (byte) 0;

	@Column(nullable = false, length = 18)
	private long birthDate = 0L;

	@Column(nullable = true, length = 32)
	private String location = "";

	@Column(nullable = true, length = 255)
	private String signature = "";

	@Column(nullable = true, length = 255)
	private String personalBrief = "";

	@Column(nullable = false, length = 11)
	private int version = 0;

	@Column(nullable = false, length = 1)
	private boolean nullity;

	@Column(nullable = false, length = 32)
	@JsonIgnore
	private String yxToken = "";

	/**
	 * 注册信息
	 */
	@Column(nullable = false, length = 32)
	@JsonIgnore
	private String registType = "";

	@Column(nullable = false, length = 18)
	@JsonIgnore
	private long registDate = 0L;

	@Column(nullable = false, length = 32)
	@JsonIgnore
	private String registIp = "";

	@Column(nullable = true, length = 32)
	@JsonIgnore
	private String lastLoginIp = "";

	@Column(nullable = true, length = 32)
	@JsonIgnore
	private String lastPcLoginIp = "";

	@Column(nullable = true, length = 50)
	@JsonIgnore
	private String lastPcLoginSerialNum = "";

	/**
	 * 是否认证
	 */
	@Column(nullable = false, length = 1)
	private boolean authed;

	@Column(nullable = false, length = 1)
	private boolean anchor;

	@Column(nullable = false, length = 11)
	private String bindMobile = "";

	@JsonIgnore
	private boolean isAndroid = false;

	@JsonIgnore
	private boolean isFaceUseInGame = true;

	@JsonIgnore
	private int userRight = 0;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getFaceSmallUrl() {
		return faceSmallUrl;
	}

	public void setFaceSmallUrl(String faceSmallUrl) {
		this.faceSmallUrl = faceSmallUrl;
	}

	public String getFaceOrgUrl() {
		return faceOrgUrl;
	}

	public void setFaceOrgUrl(String faceOrgUrl) {
		this.faceOrgUrl = faceOrgUrl;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
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

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getPersonalBrief() {
		return personalBrief;
	}

	public void setPersonalBrief(String personalBrief) {
		this.personalBrief = personalBrief;
	}

	public String getRegistType() {
		return registType;
	}

	public void setRegistType(String registType) {
		this.registType = registType;
	}

	public long getRegistDate() {
		return registDate;
	}

	public void setRegistDate(long registDate) {
		this.registDate = registDate;
	}

	public String getRegistIp() {
		return registIp;
	}

	public void setRegistIp(String registIp) {
		this.registIp = registIp;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getLastPcLoginIp() {
		return lastPcLoginIp;
	}

	public void setLastPcLoginIp(String lastPcLoginIp) {
		this.lastPcLoginIp = lastPcLoginIp;
	}

	public String getLastPcLoginSerialNum() {
		return lastPcLoginSerialNum;
	}

	public void setLastPcLoginSerialNum(String lastPcLoginSerialNum) {
		this.lastPcLoginSerialNum = lastPcLoginSerialNum;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public boolean isNullity() {
		return nullity;
	}

	public void setNullity(boolean nullity) {
		this.nullity = nullity;
	}

	public String getYxToken() {
		return yxToken;
	}

	public void setYxToken(String yxToken) {
		this.yxToken = yxToken;
	}

	public boolean isAuthed() {
		return authed;
	}

	public void setAuthed(boolean authed) {
		this.authed = authed;
	}

	public boolean isAnchor() {
		return anchor;
	}

	public void setAnchor(boolean anchor) {
		this.anchor = anchor;
	}

	public String getBindMobile() {
		return bindMobile;
	}

	public void setBindMobile(String bindMobile) {
		this.bindMobile = bindMobile;
	}

	public boolean isAndroid() {
		return isAndroid;
	}

	public void setAndroid(boolean isAndroid) {
		this.isAndroid = isAndroid;
	}

	public boolean isFaceUseInGame() {
		return isFaceUseInGame;
	}

	public void setFaceUseInGame(boolean isFaceUseInGame) {
		this.isFaceUseInGame = isFaceUseInGame;
	}

	public int getUserRight() {
		return userRight;
	}

	public void setUserRight(int userRight) {
		this.userRight = userRight;
	}

}
