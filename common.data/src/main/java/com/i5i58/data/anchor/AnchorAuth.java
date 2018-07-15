package com.i5i58.data.anchor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "AuthAnchors", uniqueConstraints = {@UniqueConstraint(columnNames="accId")})
@JsonInclude(Include.NON_DEFAULT)
public class AnchorAuth implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 156047830823562797L;

	@Id
	@Column(nullable = false, length = 32)
	private String accId = "";   //唯一主键ID
	
	@Column(nullable = false)
	private String certificateId = ""; 
	private String realName = "";
	private String imgCertificateFace = "";
	private String imgcertificateBack = "";
	private String imgPerson = "";
	 
	private int  authed = 0 ;
	
	private long createTime;
	
	private String bankCardNum = "";
	private String bankKeepPhone = "";
	
	private String location = "";
	private String bankName = "";
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public String getCertificateId() {
		return certificateId;
	}
	public void setCertificateId(String certificateId) {
		this.certificateId = certificateId;
	}
	public String getImgCertificateFace() {
		return imgCertificateFace;
	}
	public void setImgCertificateFace(String imgCertificateFace) {
		this.imgCertificateFace = imgCertificateFace;
	}
	public String getImgcertificateBack() {
		return imgcertificateBack;
	}
	public void setImgcertificateBack(String imgcertificateBack) {
		this.imgcertificateBack = imgcertificateBack;
	}
	public String getImgPerson() {
		return imgPerson;
	}
	public void setImgPerson(String imgPerson) {
		this.imgPerson = imgPerson;
	}
	public int getAuthed() {
		return authed;
	}
	public void setAuthed(int authed) {
		this.authed = authed;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getBankCardNum() {
		return bankCardNum;
	}
	public void setBankCardNum(String bankCardNum) {
		this.bankCardNum = bankCardNum;
	}
	public String getBankKeepPhone() {
		return bankKeepPhone;
	}
	public void setBankKeepPhone(String bankKeepPhone) {
		this.bankKeepPhone = bankKeepPhone;
	}

}
