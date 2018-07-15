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
@Table(name = "AuthUsers", uniqueConstraints = {@UniqueConstraint(columnNames="accId")})
@JsonInclude(Include.NON_DEFAULT)
public class AccountAuth implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2626642734844538090L;

	@Id
	@Column(nullable = false, length = 32)
	private String accId = "";   //唯一主键ID

	@Column(nullable = false, length = 18)
	private String certificateId = ""; 

	@Column(nullable = false, length = 11)
	private String realName = "";

	@Column(nullable = false, length = 1)
	private boolean authed;
	
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
	public boolean isAuthed() {
		return authed;
	}
	public void setAuthed(boolean authed) {
		this.authed = authed;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}

}
