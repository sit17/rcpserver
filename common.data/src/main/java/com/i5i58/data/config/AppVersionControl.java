package com.i5i58.data.config;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "AppVersionControls", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
@JsonInclude(Include.NON_DEFAULT)
public class AppVersionControl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4018901597252002886L;

	@Id
	@Column(nullable = false, length = 32)
	private String id = "";

	@Column(nullable = false, length = 9)
	private int mainVserion = 0;

	@Column(nullable = false, length = 9)
	private int subVersion = 0;

	@Column(nullable = false, length = 9)
	private int funcVersion = 0;

	@Column(nullable = false, length = 2)
	private int device = 0;

	@Column(nullable = false, length = 255)
	private String updateUrl = "";

	@Column(nullable = false, length = 1)
	private int status = 0;

	@Column(nullable = false, length = 255)
	private String versionDescribe = "";

	@Column(nullable = false, length = 32)
	private String appleRelease = "";

	@Column(nullable = false, length = 32)
	private String applePayType = "";

	@Column(nullable = false, length = 32)
	private String rnVersion="";

	private long collectTime= 0L;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMainVserion() {
		return mainVserion;
	}

	public void setMainVserion(int mainVserion) {
		this.mainVserion = mainVserion;
	}

	public int getSubVersion() {
		return subVersion;
	}

	public void setSubVersion(int subVersion) {
		this.subVersion = subVersion;
	}

	public int getFuncVersion() {
		return funcVersion;
	}

	public void setFuncVersion(int funcVersion) {
		this.funcVersion = funcVersion;
	}

	public int getDevice() {
		return device;
	}

	public void setDevice(int device) {
		this.device = device;
	}

	public String getUpdateUrl() {
		return updateUrl;
	}

	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getVersionDescribe() {
		return versionDescribe;
	}

	public void setVersionDescribe(String versionDescribe) {
		this.versionDescribe = versionDescribe;
	}

	public long getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(long collectTime) {
		this.collectTime = collectTime;
	}

	public String getAppleRelease() {
		return appleRelease;
	}

	public void setAppleRelease(String appleRelease) {
		this.appleRelease = appleRelease;
	}

	public String getApplePayType() {
		return applePayType;
	}

	public void setApplePayType(String applePayType) {
		this.applePayType = applePayType;
	}

	public String getRnVersion() {
		return rnVersion;
	}

	public void setRnVersion(String rnVersion) {
		this.rnVersion = rnVersion;
	}

}
