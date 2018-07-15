package com.i5i58.data.config;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "PlatformConfigs", uniqueConstraints = { @UniqueConstraint(columnNames = { "cKey" }) })
@JsonInclude(Include.NON_DEFAULT)
public class PlatformConfig implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1514075622587887247L;

	@Id
	@Column(nullable = false, length = 128)
	private String cKey = "";

	@Column(length = 2048)
	private String cValue = "";

	@Column(length = 255)
	private String cDesc = "";

	public String getcKey() {
		return cKey;
	}

	public void setcKey(String cKey) {
		this.cKey = cKey;
	}

	public String getcValue() {
		return cValue;
	}

	public void setcValue(String cValue) {
		this.cValue = cValue;
	}

	public String getcDesc() {
		return cDesc;
	}

	public void setcDesc(String cDesc) {
		this.cDesc = cDesc;
	}

}
